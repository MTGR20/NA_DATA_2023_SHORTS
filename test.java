import java.util.Random;
public class test {
    public static void main(String[] args) {

        //비트마스킹 잊어버린 김에 테스트 좀..

        int list_size = 6;

        int check = (int) Math.pow(2, list_size) - 1;

//        check = 61;

        int random = 1;

        int mask = (1<<random);

        System.out.println((check & mask));

//        //실제론
//        while ((check & mask) != mask){
//            random.nextInt(list_size);
//        }
//        //이후 check^=mask;

        if ((check & mask) == mask){
            check ^= mask;
            System.out.println("이 때만 실행: " + check);
        }
        else {
            // 다시 랜덤 돌리기 nextInt
            System.out.println("다음 랜덤!");
        }
    }
}
