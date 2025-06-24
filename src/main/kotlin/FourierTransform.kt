import org.kotlinmath.Complex

abstract class FourierTransform {

    abstract fun toFrequencyDomain(spatialData: Array<IntArray>): Array<Array<FrequencyDomain>>

    abstract fun f(freqX: Int, freqY: Int, spatialData: Array<IntArray>): Complex

    abstract fun toSpatialDomain(frequencyData: Array<IntArray>): Array<IntArray>

    abstract fun fInverse(spaceX: Int, spaceY: Int, frequencyData: Array<IntArray>): Complex

}