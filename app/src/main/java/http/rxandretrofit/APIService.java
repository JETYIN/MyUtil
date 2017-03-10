package http.rxandretrofit;

import java.util.List;

import http.rxandretrofit.etity.BookListTags;
import http.rxandretrofit.etity.BookSource;
import http.rxandretrofit.etity.Recommend;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/10.
 */

public interface APIService {

    /**定义进行网络请求--返回的被观察者**/
    @GET("/book/recommend")
    Observable<Recommend> getRecomend(@Query("gender") String gender);

    /**一个参数和两个参数的比对区别**/
    @GET("/atoc")
    Observable<List<BookSource>> getBookSource(@Query("view") String view, @Query("book") String book);
    /**
     * 获取主题书单标签列表
     *
     * @return
     */
    @GET("/book-list/tagType")
    Observable<BookListTags> getBookListTags();
}
