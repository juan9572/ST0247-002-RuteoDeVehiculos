public class Algorithm {

    public static boolean DFSColorFC(DigraphAM am) {
        int color[] = new int[am.size];
        for(int i = 0; i < am.size; i++){
            for(int j = 0; j < am.size; j++ ){
                if(am.mat[i][j]== 1){
                    if(color[i] != 0 && color [j] == 0){
                        if(color[i] == -1){
                            color[j] = -2;
                        }else{
                            color[j] = -1;
                        }
                    }else{
                        if(color[i] == 0) color[i] = -1;
                        if(color[j]== 0) color[j] = -2;
                    }
                }
            }
        }
        for(int i = 0; i < am.size; i++){
            for(int j = 0; j < am.size; j++ ){
                if(am.mat[i][j] == 1){
                    if(color[i] == color[j]){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}