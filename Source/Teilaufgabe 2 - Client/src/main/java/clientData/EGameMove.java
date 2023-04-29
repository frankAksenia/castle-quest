package clientData;

public enum EGameMove {
	UP(-1,0),
	DOWN(1,0),
	LEFT(0,-1),
	RIGHT(0,1);
	
	 private final int x;
	 private final int y;
	
	EGameMove(int x, int y) {
        this.x = x;
        this.y = y;
    }

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
