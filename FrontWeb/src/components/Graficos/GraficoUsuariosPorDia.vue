<script>
import { Line } from "vue-chartjs"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, LineController, LineElement, PointElement, TimeScale} from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';
import annotationPlugin from 'chartjs-plugin-annotation';
import 'chartjs-adapter-luxon';
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels, annotationPlugin, LineController, LineElement, PointElement, TimeScale)

ChartJS.defaults.set('plugins.datalabels', {
    color: 'rgb(0, 0, 0)'
})

export default {
    name: 'graficoUsuariosPorDia',
    components: {
        Line
    },
    data() {
        return {
            
        }
    },
    props: {
        data: Object,
        options: Object
    },
    methods: {
        handleClick(ev){
            // const ar = this.$refs.graph.chart.getElementsAtEventForMode(ev, 'x', {intersect: false, axis: 'x'}, true)
            // if(ar.length == 1) this.$emit('mostrarInfoIndex', ar[0].index)
            
        }
    },
    // emits: ['mostrarInfoIndex']
}
</script>

<template>
    <div id="contenedorGrafico2" >
        <Line ref="graph" id="barra" :options="options" :data="data" />
    </div>

</template>

<style scoped>
#contenedorGrafico2 {
    width: 100%;
    height: 290px;
    margin: 0;

}
</style>