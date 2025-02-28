package Model;

public class Orders {
    String d_order;
    String d_targetName;
    Integer d_noOfArmiesToMove;

    public Orders(String p_order, String p_targetName, Integer p_noOfArmiesToMove){
        this.d_order = p_order;
        this.d_targetName = p_targetName;
        this.d_noOfArmiesToMove = p_noOfArmiesToMove;
    }

    public String getD_order() {
        return d_order;
    }

    public String getD_targetName() {
        return d_targetName;
    }

    public Integer getD_noOfArmiesToMove() {
        return d_noOfArmiesToMove;
    }

    public void setD_order(String d_order) {
        this.d_order = d_order;
    }

    public void setD_targetName(String d_targetName) {
        this.d_targetName = d_targetName;
    }

    public void setD_noOfArmiesToMove(Integer d_noOfArmiesToMove) {
        this.d_noOfArmiesToMove = d_noOfArmiesToMove;
    }
}
