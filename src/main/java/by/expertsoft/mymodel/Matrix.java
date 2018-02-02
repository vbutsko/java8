package by.expertsoft.mymodel;

public class Matrix {
    private int n, m;
    private long data[];

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        data = new long[n * m];
    }

    public Matrix(int n, int m, long data[]) {
        this.n = n;
        this.m = m;
        this.data = data;
    }

    public Matrix multiple(Matrix matrix) {
        if (m != matrix.getN()) {
            throw new RuntimeException("can't multiply matrixes");
        }

        Matrix result = new Matrix(n, matrix.getM());
        for (int i = 0; i < n; ++i) {
            for(int j = 0; j < m; ++j) {
                for(int k = 0; k < matrix.getM(); ++k) {
                    result.setAt(i, k, result.getAt(i, k) + getAt(i, j) * matrix.getAt(j, k));
                }
            }
        }
        return result;
    }

    public long getAt(int i, int j) {
        checkBoundaries(i, j);
        return data[i * m + j];
    }

    public void setAt(int i, int j, long val) {
        checkBoundaries(i, j);
        data[i * m + j] = val;
    }

    private void checkBoundaries(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= m) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }
}
