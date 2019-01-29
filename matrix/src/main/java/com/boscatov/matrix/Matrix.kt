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
        var sign = 1
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
    val a3 = a1.inverse()
    val a4 = Matrix(2, 2)
}