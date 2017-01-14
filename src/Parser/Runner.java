package Parser;

/**
 * Created by AndrewIrwin on 14/01/2017.
 */



public class Runner {
    public static void main(String[] args) throws Throwable{
        Context ctx = new Context();
        ContextParser cp = new ContextParser(ctx);
        cp.init();

        System.out.println(ctx);
    }
}

