<script>
import { Bar } from "vue-chartjs"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, TimeScale} from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';
import annotationPlugin from 'chartjs-plugin-annotation';
import 'chartjs-adapter-luxon';
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels, annotationPlugin, TimeScale)

ChartJS.defaults.set('plugins.datalabels', {
    color: 'rgb(0, 0, 0)'
})

export default {
    name: 'graficoGastosPorMes',
    components: {
        Bar,
    },
    data() {
        return {
            
        }
    },
    props: {
        data: Object,
        options: Object,
        width: Number,
        height: Number
    },
    methods: {
        handleClick(ev){
            // const ar = this.$refs.graph.chart.getElementsAtEventForMode(ev, 'x', {intersect: false, axis: 'x'}, true)
            // if(ar.length == 1) this.$emit('mostrarInfoIndex', ar[0].index)
            
        },
        forceUpdate(){
            this.$forceUpdate()
        },
        getStyle(){
            return 'width: ' + this.width + 'px; height: ' + this.height + 'px;'
        }
    },
    // emits: ['mostrarInfoIndex']
}
</script>

<template>
    <div id="contenedorGrafico2" :style="getStyle()" >
        <Bar  ref="graph" id="barra" :options="options" :data="data" />
    </div>

</template>

<style scoped>
#contenedorGrafico2 {
    width: 630px;
    height: 230px;
    margin: 0;

}
</style>