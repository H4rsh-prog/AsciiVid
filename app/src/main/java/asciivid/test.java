package asciivid;
public class test {
    public static void main(String[] args) {
        for(int i =0;i<70;i++){
            System.out.print("\r@");
            for(int j=1;j<269;j++){
                try {
                    System.out.print("@");
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
}
