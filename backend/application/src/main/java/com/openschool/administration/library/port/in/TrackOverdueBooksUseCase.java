package com.openschool.administration.library.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.BookLoan;

public interface TrackOverdueBooksUseCase {
    PageResult<BookLoan> trackOverdueBooks(PageInfo pageInfo);
}
