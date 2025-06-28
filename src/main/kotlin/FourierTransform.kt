abstract class FourierTransform {

    abstract fun toFrequencyDomain(spatialData: Array<IntArray>): Array<Array<FrequencyDomain>>

    abstract fun toSpatialDomain(frequencyData: Array<Array<FrequencyDomain>>): Array<IntArray>

}