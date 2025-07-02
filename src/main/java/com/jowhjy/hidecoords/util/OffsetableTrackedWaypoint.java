package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;

public interface OffsetableTrackedWaypoint {
    default OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return this;
    }
}
