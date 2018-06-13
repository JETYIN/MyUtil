package utils.singleton;

/**
 * Created by Administrator on 2018/5/14.泛型单例，使用于并发编程
 */

public class EventManager {

    private EventManager() {

    }

    public enum SingleTon {

        INSTANCE;
        private EventManager instance;

        private SingleTon() {
            instance = new EventManager();
        }

        public EventManager getInstance() {
            return instance;
        }
    }


}
