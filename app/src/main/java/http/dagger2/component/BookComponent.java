package http.dagger2.component;

import dagger.Component;
import work.dylan.com.rxdemo.dagger2.test.BookActivity;

/**
 * Created by Administrator on 2018/6/26.
 */


@Component(dependencies = AppComponent.class)
public interface BookComponent {//FIXME 各个模块集成自appComponent模块

    BookActivity inject(BookActivity activity);//FIXME 各模块的setComponent方法中实现各不相同
}
