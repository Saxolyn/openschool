package com.openschool.administration.library.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.BookLoan;
import java.util.UUID;

public interface ViewUserBorrowingHistoryUseCase {
    PageResult<BookLoan> viewUserBorrowingHistory(UUID userId, PageInfo pageInfo);
}
