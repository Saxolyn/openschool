package com.openschool.administration.library.port.in;

import com.openschool.common.pageable.PageInfo;
import com.openschool.common.pageable.PageResult;
import com.openschool.domain.library.Book;

public interface GetBookListUseCase {
    PageResult<Book> getBookList(PageInfo pageInfo);
}
