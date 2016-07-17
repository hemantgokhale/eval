public class Work {

    public static final String url2 = "http://127.0.0.1:9090/delayed2";
    public static final String url10 = "http://127.0.0.1:9090/delayed10";
    

    /**
     * Simulate some non-blocking work that involves using local compute resources
     *
     * @param weight indicates the amount of work to be done. Minimum value is 1
     */
    static public String local(int weight) {
        if (weight < 1) {
            weight = 1;
        }

        for (int i = 0; i < weight; i++) {
            String a = "abc";
            for (int j = 0; j < 100; j++) {
                a = a + (j * j);
            }
        }
        
        return "Completed local work of weight " + weight + "\n";
    }
}
