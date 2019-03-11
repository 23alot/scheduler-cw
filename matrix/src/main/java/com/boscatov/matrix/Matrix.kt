package com.boscatov.matrix

class Matrix() {
    private val matrix = arrayListOf<MutableList<Double>>()

    constructor(N: Int, M: Int) : this() {
        for (i in 0 until N) {
            val row = ArrayList<Double>()
            for (j in 0 until M) {
                row.add(0.0)
            }
            matrix.add(row)
        }
    }

    val T: Matrix
        get() {
            return transpose()
        }

    fun addRow(row: MutableList<Double>) {
        matrix.add(row)
    }

    private fun transpose(): Matrix {
        val transposedMatrix = Matrix()
        for (z in 0 until matrix[0].size) {
            val row = arrayListOf<Double>()
            for (i in 0 until matrix.size) {
                row.add(matrix[i][z])
            }
            transposedMatrix.addRow(row)
        }
        return transposedMatrix
    }

    private fun cofactor(m: Matrix, n: Int, p: Int, q: Int): Matrix {
        val temp = Matrix(m.matrix.size, m.matrix.size)
        var i = 0
        var j = 0
        for (row in 0 until n) {
            for (col in 0 until n) {
                if (row != p && col != q) {
                    temp.matrix[i][j++] = m.matrix[row][col]

                    if (j == m.matrix.size - 1) {
                        j = 0
                        i++
                    }
                }
            }
        }

        return temp
    }

    private fun determinant(m: Matrix, n: Int): Double {
        var D = 0.0
        if (n == 1) {
            return m.matrix[0][0]
        }
        if (n == 2) {
            return m.matrix[0][0] * m.matrix[1][1] - m.matrix[0][1] * m.matrix[1][0]
        }
        var sign = 1
        for (i in 0 until n) {
            val cofactor = cofactor(m, n, 0, i)
            val deter = determinant(cofactor, n - 1)
            D += sign * m.matrix[0][i] * deter
            sign = -sign
        }

        return D
    }

    private fun adjoint(m: Matrix): Matrix {
        var sign: Int
        val n = m.matrix.size
        val adj = Matrix(n, n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                val temp = cofactor(m, n, i, j)
                sign = if ((i + j) % 2 == 0) 1 else -1
                adj.matrix[i][j] = sign * determinant(temp, n - 1)
            }
        }
        return adj
    }

    fun inverse(): Matrix {
        val n = matrix.size
        val inverse = Matrix(n, n)
        val det = determinant(this.T, n)
        val adj = adjoint(this.T)
        for (i in 0 until n) {
            for (j in 0 until n) {
                inverse.matrix[i][j] = adj.matrix[i][j] / det
            }
        }
        return inverse
    }

    operator fun iterator(): Iterator<MutableList<Double>> {
        return matrix.iterator()
    }

    operator fun times(other: Matrix): Matrix {
        if (other.matrix.size != this.matrix[0].size) {
            throw IllegalArgumentException("Not valid dimensions")
        }

        val result = Matrix(this.matrix.size, other.matrix[0].size)
        for (i in 0 until result.matrix.size) {
            for (j in 0 until result.matrix[0].size) {
                for (k in 0 until this.matrix[0].size) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j]
                }
            }
        }
        return result
    }

    fun toDoubleArrayList(): ArrayList<Double> {
        val result = arrayListOf<Double>()
        for (row in matrix) {
            for(value in row) {
                result.add(value)
            }
        }
        return result
    }
}

fun matrixOf(vararg rows: MutableList<Double>): Matrix {
    val matrix = Matrix()
    for (row in rows) {
        matrix.addRow(row)
    }
    return matrix
}

fun main(arg: Array<String>) {
    val a1 = matrixOf(
        mutableListOf(-1.0, 2.0, -2.0),
        mutableListOf(2.0, -1.0, 5.0),
        mutableListOf(3.0, -2.0, 4.0)
    )
    val a2 = matrixOf(
        mutableListOf(0.0, 4.0, -2.0),
        mutableListOf(-4.0, -3.0, 0.0)
    )
    val a2_2 = matrixOf(
        mutableListOf(0.0, 1.0),
        mutableListOf(1.0, -1.0),
        mutableListOf(2.0, 3.0)
    )

    val result = a2 * a2_2
    val a3 = a1.inverse()
    val a4 = Matrix(2, 2)

    val X = matrixOf(
        mutableListOf(2.0, 3.0),
        mutableListOf(-1.0, 4.0)
    )
    val y = matrixOf(
        mutableListOf(1.0),
        mutableListOf(2.0)
    )
    val res = testNN(X, y)
    val a = 1

}

fun testNN(X: Matrix, y: Matrix): Matrix {
    val XT = X.T
    val w = (XT * X).inverse() * XT * y
    return w.T
}