package http.dagger2;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import work.dylan.com.rxdemo.dagger2.test.Book;

/**
 * Created by Administrator on 2018/6/26.
 */

public interface Api {

    @GET("/student/select/{bookId}")
    Observable<Book> getBookId(@Path("bookId") String bookId);//query为附带在url后的参数


}
