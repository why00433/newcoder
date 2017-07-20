package cn.edu.seu.practice.singleton;
/*
* 静态内部类方式实现懒汉单例的线程安全
* */
public class Singleton2 {

    private Singleton2(){}
    //静态内部类中持有单例对象实例。
    private static class SingletonHolder{
        private static Singleton2 instance=new Singleton2();
    }

    public static Singleton2 getInstance(){
        return SingletonHolder.instance;
    }
}
