package server.model;

public enum EMapShape {
	HORIZONTAL_FIRST(10),
	HORIZONTAL_SECOND(10),
	VERTICAL_FIRST(5),
	VERTICAL_SECOND(5);

    private int value;

    private EMapShape(int value) {
        this.value = value;
    }

    public int getMapAdjustingFactor() {
        return this.value;
    }
}
