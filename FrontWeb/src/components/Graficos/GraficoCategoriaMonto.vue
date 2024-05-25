<script>
import { Bar, Pie } from "vue-chartjs"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, PieController, ArcElement, RadialLinearScale, TimeScale } from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';
import annotationPlugin from 'chartjs-plugin-annotation';
import 'chartjs-adapter-luxon';
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels, annotationPlugin, PieController, ArcElement, RadialLinearScale, TimeScale)

ChartJS.defaults.set('plugins.datalabels', {
    color: 'rgb(0, 0, 0)'
})

export default {
    name: 'graficoCategoriaMonto',
    components: {
        Bar,
        Pie
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
    <div id="contenedorGrafico2">
        <Pie ref="graph" id="pie" :options="options" :data="data" />
    </div>

</template>

<style scoped>
#contenedorGrafico2 {
    width: 290px;
    height: 290px;
    margin: 0;

}
</style>