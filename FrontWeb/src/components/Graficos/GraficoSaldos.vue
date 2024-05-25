<script>
import { Bar } from "vue-chartjs"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';
import annotationPlugin from 'chartjs-plugin-annotation';
ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, ChartDataLabels, annotationPlugin)

ChartJS.defaults.set('plugins.datalabels', {
    color: 'rgb(0, 0, 0)'
})

export default {
    name: 'graficoSaldos',
    components: {
        Bar
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
            //
            //
            const ar = this.$refs.graph.chart.getElementsAtEventForMode(ev, 'x', {intersect: false, axis: 'x'}, true)
            if(ar.length == 1) this.$emit('mostrarInfoIndex', ar[0].index)
            
        }
    },
    emits: ['mostrarInfoIndex']
}
</script>

<template>
    <div id="contenedorGrafico">
        <Bar ref="graph" id="barras" :options="options" :data="data" />
    </div>
</template>

<style scoped>
#contenedorGrafico {
    width: 98%;
    height: 240px;
    border-width: 1px;
    border-color: rgb(183, 183, 183);
    border-style: solid;
    margin: 20px;
    margin-top: 0px;
    margin-bottom: 10px;
    border-radius: 5px;
    padding-top: 6px;
}
</style>