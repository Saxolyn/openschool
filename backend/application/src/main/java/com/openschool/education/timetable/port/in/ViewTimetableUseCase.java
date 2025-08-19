package com.openschool.education.timetable.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.timetable.Timetable;
import java.util.UUID;

public interface ViewTimetableUseCase {
    Timetable viewTimetable(UUID timetableId);
    PageResult<Timetable> viewTimetablesByClass(UUID classId, PageInfo pageInfo);
}

