package model;

/*import java.util.Comparator;*/


public class State {//implements Comparable<State> {

    Object state;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public State(Object state) {
        this.state = state;
    }

}






/* @Override
public boolean equals(Object eqobj){
	if (!(eqobj instanceof State))
		return false;
	State obj = (State)(eqobj);
	return mState.equals(obj.mState);
}

@Override
public String toString(){
	return "State is: " + mState;
}

@Override
public int hashCode() {
	return mState.toString().hashCode();

}

@Override
public int compareTo(State o) {
        return (int) (mF - o.mF);
}

	*/