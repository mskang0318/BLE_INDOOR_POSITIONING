package com.example.localizationserdar.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.localizationserdar.utils.ColorUtil;
import com.nexenio.bleindoorpositioning.ble.advertising.AdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.beacon.Beacon;
import com.nexenio.bleindoorpositioning.ble.beacon.signal.ArmaFilter;
import com.nexenio.bleindoorpositioning.ble.beacon.signal.KalmanFilter;
import com.nexenio.bleindoorpositioning.ble.beacon.signal.MeanFilter;
import com.nexenio.bleindoorpositioning.ble.beacon.signal.WindowFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RssiFilterLineGraph extends BeaconLineGraph {

    public static List<WindowFilter> filterList;

    public RssiFilterLineGraph(Context context) {
        super(context);
    }

    public RssiFilterLineGraph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initialize() {
        super.initialize();
        filterList = new ArrayList<>(Arrays.asList(
                new MeanFilter(),
                new ArmaFilter(),
                new KalmanFilter()
        ));
    }

    @Override
    protected void drawBeacon(Canvas canvas, Beacon beacon) {
        int filterIndex = 0;
        for (WindowFilter filter : filterList) {
            prepareDraw(beacon);
            linePaint.setShader(createLineShader(ColorUtil.getBeaconColor(filterIndex)));

            for (AdvertisingPacket advertisingPacket : (List<AdvertisingPacket>) beacon.getAdvertisingPackets()) {
                if (advertisingPacket.getTimestamp() < minimumAdvertisingTimestamp) {
                    continue;
                }
                currentLinePoint = getPointFromAdvertisingPacket(beacon, advertisingPacket, currentLinePoint, filter);
                drawCircleForPreviousPoint(canvas);
                drawNextPoint(canvas, advertisingPacket);
            }

            filterIndex++;
            fadeLastPoint(canvas);
        }
    }

    protected float getValue(Beacon beacon, AdvertisingPacket advertisingPacket, WindowFilter filter) {
        float filteredRssi;

        if (windowLength == 0) {
            filteredRssi = advertisingPacket.getRssi();
        } else {
            filter.setMaximumTimestamp(advertisingPacket.getTimestamp());
            filter.setMinimumTimestamp(advertisingPacket.getTimestamp() - windowLength);
            filteredRssi = filter.filter(beacon);
        }

        return processReturnValue(beacon, advertisingPacket, filteredRssi);
    }

    protected PointF getPointFromAdvertisingPacket(Beacon beacon, AdvertisingPacket advertisingPacket, PointF point, WindowFilter filter) {
        if (point == null) {
            point = new PointF();
        }
        point.x = xAxisStartPoint.x + ((xAxisEndPoint.x - xAxisStartPoint.x) * (xAxisRange - (System.currentTimeMillis() - advertisingPacket.getTimestamp()))) / xAxisRange;
        point.y = yAxisStartPoint.y - ((yAxisStartPoint.y - yAxisEndPoint.y) * (getValue(beacon, advertisingPacket, filter) - (float) yAxisMinimumAnimator.getAnimatedValue())) / yAxisRange;
        return point;
    }

}