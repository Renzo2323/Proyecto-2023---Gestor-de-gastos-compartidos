<script>
import GraficoCategoriaMonto from '../components/Graficos/GraficoCategoriaMonto.vue';
import GraficoGastosPorMes from '../components/Graficos/GraficoGastosPorMes.vue';
import GraficoUsuariosPorDia from '../components/Graficos/GraficoUsuariosPorDia.vue';
import GraficoActividadPorMes from '../components/Graficos/GraficoActividadPorMes.vue';
import Boton from '../components/Botones/Boton.vue'
import axios from 'axios';
import { DateTime } from 'luxon'

import { saveAs } from 'file-saver';
export default {
    name: 'principalUsuario',
    components: {
        GraficoCategoriaMonto,
        GraficoGastosPorMes,
        GraficoUsuariosPorDia,
        GraficoActividadPorMes,
        Boton
    },
    data() {
        return {
            grupos: [
            ],

            grupoSeleccionado: null,
            year: 2024,
            tieneGastos: true,
            vistaPDF: false
        }
    },
    methods: {
        getData1() {
            let nombresGrupo = []
            let montosTotales = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            this.grupos.forEach(grupo => {

                nombresGrupo.push(grupo.nombre + "-" + grupo.moneda)
                montosTotales.push(grupo.total)

            })
            const obj = {
                labels: nombresGrupo,
                datasets: [{
                    label: 'aassadas',
                    data: montosTotales,
                    backgroundColor: coloresRandom,
                    // borderColor: 'black',
                    borderWidth: 1
                }]
            }
            return obj
        },
        getOptions1() {
            return {
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 20,
                        right: 20,
                        top: 20,
                        bottom: 20
                    }
                },
                plugins: {
                    tooltip: {
                        enabled: false
                    },
                    legend: { 
                        display: this.vistaPDF ? true : false,
                        position: "right",
                        labels:{
                            generateLabels: (chart) =>{
                                const datasets = chart.data.datasets;
                                return datasets[0].data.map((data, i) => ({
                                text: `${this.dataProcesada(data, chart.data.labels[i])}`,
                                fillStyle: datasets[0].backgroundColor[i],
                                index: i
                                }))
                            }
                        }
                    },
                    datalabels: {
                        display: function (context) {
                            return context.active
                        },
                        backgroundColor: 'rgb(255, 255, 255)',
                        borderRadius: 50,
                        borderColor: 'rgb(0, 0, 0)',
                        borderWidth: 1,
                        font: { family: "Lexend" },
                        anchor: 'center',
                        align: 'bottom',
                        labels: {
                            title: {
                                backgroundColor: 'rgb(255, 61, 96)',
                                color: 'rgb(255, 255, 255)',
                                borderWidth: 1,
                                borderColor: '#8f183e',
                                offset: -20,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => (" " + ctx.chart.data.labels[ctx.dataIndex].substring(0,ctx.chart.data.labels[ctx.dataIndex].length -4 ) + " ")
                            },
                            value: {
                                offset: 3,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    let label = ctx.chart.data.labels[ctx.dataIndex]
                                    let moneda = label.split('-').reverse()[0]
                                    return this.signoDinero(moneda) + Math.abs(val) + " "

                                }
                            }
                        }
                    },
                },

            }
        },
        dataProcesada(data, label){
            let moneda = label.split('-').reverse()[0]
            let resto = label.split('-').reverse()[1]
            return resto + " " + this.signoDinero(moneda) + data + " "
        },
        getDates(fechas) {
            let datetimes = []
            fechas.forEach(f => {
                let year = f.split('-')[0]
                if (year != this.year) {
                    return
                }
                let month = f.split('-')[1]
                let day = f.split('-')[2]
                datetimes.push(DateTime.fromObject({ year: year, month: month, day: day }))
            })
            if(datetimes.length > 0){
                this.tieneGastos = true
            }else{
                this.tieneGastos = false
            }
            return datetimes
        },
        getData2() {
            let fechas = []
            let montosTotales = []
            this.grupoSeleccionado.totalGastosPorMes.forEach(tgm => {
                fechas.push(tgm.fecha)
                montosTotales.push(tgm.montoTotal)
            })
            let aa = this.getDates(fechas)

            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            const obj = {
                labels: aa,
                datasets: [{
                    label: 'aassadas',
                    data: montosTotales,
                    backgroundColor: coloresRandom,
                    // borderColor: 'black',
                    borderWidth: 1
                }]
            }
            return obj
        },
        getOptions2() {
            return {
                barThickness: 40,
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'month',
                            displayFormats: {
                                month: 'MMM'
                            }
                        },
                        ticks: {
                            source: 'auto',
                        },

                    },
                    // adapters: {
                    //     date: {
                    //         // locale: es
                    //     }
                    // }
                },
                layout: {
                    padding: {
                        left: 0,
                        right: 0,
                        top: 0,
                        bottom: 20
                    }
                },
                plugins: {
                    tooltip: {
                        enabled: false
                    },
                    legend: {
                        display: false,
                    },
                    datalabels: {
                        display: function (context) {
                            return context.active
                        },
                        backgroundColor: 'rgb(255, 255, 255)',
                        borderRadius: 50,
                        borderColor: 'rgb(0, 0, 0)',
                        borderWidth: 1,
                        font: { family: "Lexend" },
                        anchor: 'start',
                        align: 'bottom',
                        labels: {
                            value: {
                                offset: 27,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    return this.signoDinero(this.grupoSeleccionado.moneda) + Math.abs(val) + " "

                                }
                            }
                        }
                    },
                },
            }
        },
        getData3() {
            let nombresCategoria = []
            let montosTotales = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            this.grupoSeleccionado.categorias.forEach(categoria => {

                nombresCategoria.push(categoria.nombre)
                montosTotales.push(categoria.montoTotal)

            })
            const obj = {
                labels: nombresCategoria,
                datasets: [{
                    label: 'aassadas',
                    data: montosTotales,
                    backgroundColor: coloresRandom,
                    // borderColor: 'black',
                    borderWidth: 1
                }]
            }
            return obj
        },
        getOptions3() {
            return {
                layout: {
                    padding: {
                        left: 20,
                        right: 20,
                        top: 15,
                        bottom: 20
                    }
                },
                plugins: {
                    tooltip: {
                        enabled: false
                    },
                    legend: { 
                        display: this.vistaPDF ? true : false,
                        position: "bottom",
                        labels:{
                            generateLabels: (chart) =>{
                                const datasets = chart.data.datasets;
                                return datasets[0].data.map((data, i) => ({
                                text: `${chart.data.labels[i]} ${this.signoDinero(this.grupoSeleccionado.moneda)}${data}`,
                                fillStyle: datasets[0].backgroundColor[i],
                                index: i
                                }))
                            }
                        }
                    },
                    datalabels: {
                        display: function (context) {
                            return context.active
                        },
                        backgroundColor: 'rgb(255, 255, 255)',
                        borderRadius: 50,
                        borderColor: 'rgb(0, 0, 0)',
                        borderWidth: 1,
                        font: { family: "Lexend" },
                        anchor: 'center',
                        align: 'bottom',
                        labels: {
                            title: {
                                backgroundColor: 'rgb(255, 61, 96)',
                                color: 'rgb(255, 255, 255)',
                                borderWidth: 1,
                                borderColor: '#8f183e',
                                offset: -20,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => (" " + ctx.chart.data.labels[ctx.dataIndex] + " ")
                            },
                            value: {
                                offset: 3,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    return this.signoDinero(this.grupoSeleccionado.moneda) + Math.abs(val) + " "

                                }
                            }
                        }
                    },
                },

            }
        },
        shuffle(array) {
            let currentIndex = array.length, randomIndex;

            while (currentIndex > 0) {

                randomIndex = Math.floor(this.getRndBias(0, 1, 1, 1) * currentIndex);
                currentIndex--;

                [array[currentIndex], array[randomIndex]] = [
                    array[randomIndex], array[currentIndex]];
            }

            return array;
        },
        getRndBias(min, max, bias, influence) {
            var rnd = Math.random() * (max - min) + min,
                mix = Math.random() * influence;
            return rnd * (1 - mix) + bias * mix;
        },

        signoDinero(moneda) {
            switch (moneda) {
                case "USD":
                    return "$"
                case "UYU":
                    return "$U"
                case "ARS":
                    return "$"
                case "EUR":
                    return "€"
                case "GBP":
                    return "£"
                case "JPY":
                    return "¥"
            }
        },
        async fetchEstadisticas(){
            const url = import.meta.env.VITE_API + "/api/reportes/usf/getEstadisticasUsuario"
            const response = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    // debugger
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.grupos = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        },
        sumaTotales(){
            let suma = 0
            this.grupos.forEach(g=>{
                suma += g.total
            })
            return suma
        },
        exportarPDF(){
            this.$emit('toggleLeftBar', false)
            this.$emit('toggleNavBar', false)
            this.$emit('togglePerfil', false)
            this.vistaPDF = true

            setTimeout(function() { print(); }, 1000);
            
        },
        handleBeforePrint(event){
        },
        handleAfterPrint(event){
            this.$emit('toggleLeftBar', true)
            this.$emit('toggleNavBar', true)
            this.$emit('togglePerfil', true)
            this.vistaPDF = false
        },
        convertToCSV(arr) {
            const array = [Object.keys(arr[0])].concat(arr)

            return array.map(it => {
                return Object.values(it).toString()
            }).join('\n')
        },
        exportarCSV(){
            let zip = new JSZip()
            zip.file('TotalGastosPorMes.csv', this.convertToCSV(this.grupoSeleccionado.totalGastosPorMes) ).
            file('TotalGastosPorCategorias.csv', this.convertToCSV(this.grupoSeleccionado.categorias))
            let promise = zip.generateAsync({type : "blob"})
            .then(function(blob){
                const nombreUsr = this.nombreUsuario
                const fecha = (new Date()).toLocaleDateString();
                const nombre = 'Estadisticas' + nombreUsr + "-" + this.grupoSeleccionado.nombre + "-" + fecha + '.zip' 
                saveAs(blob, nombre)
            }.bind(this))
            
            
        }
    },
    mounted(){
        this.fetchEstadisticas()
    },
    created(){
        window.addEventListener("beforeprint", this.handleBeforePrint)

        window.addEventListener("afterprint", this.handleAfterPrint)
    },
    unmounted(){
        window.removeEventListener("beforeprint", this.handleBeforePrint)
        window.removeEventListener("afterprint", this.handleAfterPrint)
    },
    watch: {
        year(viejo, nuevo) {
            this.$refs.graficoGastosPorMes.forceUpdate()
        }
    },
    computed: {

    },
    props:{
        nombreUsuario : String
    }
}
</script>

<template>
    <div id="contenedor1">
        <div>
            <div style="margin-bottom: 100px" v-if="grupos.length > 0 && sumaTotales() > 0">
                <h2>Estadísticas personales</h2>
                <div style="display:flex; gap: 15px; margin-top: 15px;">
                    <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center; width:100%">
                        <h3 style="margin-bottom: 0; padding-bottom: 0;">Tus gastos por grupo</h3>
                        <GraficoCategoriaMonto style="width: 600px" ref="graficoCategoriaMonto" :data="getData1()" :options="getOptions1()" />
                    </div>
                    <!-- <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                        <h3 style="margin-bottom: 0; padding-bottom: 0;">Algun otro dato general??</h3>

                        
                    </div> -->
                </div>


                <div v-if="!vistaPDF" style="display:flex; align-items: center; justify-content: center;">
                    <h3 style="margin-top: 15px;">En el grupo</h3>
                    <div style="width: 200px;">
                        <v-select style="margin-left: 15px; font-size: 50px;" hide-details v-model="grupoSeleccionado"
                            item-title="nombre" return-object item-value="id" :items="grupos" variant="underlined">
                            <template v-slot:item="{ props, item }">
                                <v-list-item v-bind="props" title="">
                                    <template v-slot:prepend>
                                        {{ item.raw.nombre }}
                                    </template>
                                </v-list-item>
                            </template>
                        </v-select>
                    </div>
                </div>
                <Transition>
                <div v-if="grupoSeleccionado != null">
                    <div v-if="vistaPDF" style="display:flex; align-items: center; justify-content: center;">
                    <h2 style="margin-top: 15px">
                        {{grupoSeleccionado.nombre}}
                    </h2>
                        
                    </div>
                    <div id="contenedorGrafico"
                        style="display: flex; flex-direction: row; justify-content: space-between; align-items: center; margin-top: 15px">
                        <div>
                            <h3 style="margin-bottom: 0; padding-bottom: 0;">Has gastado</h3>
                            <div style="font-size: 30px;">
                                {{ signoDinero(this.grupoSeleccionado.moneda) }}{{ grupoSeleccionado.total }}
                            </div>
                        </div>
                        

                        <div v-if="!vistaPDF" style="display:flex; gap: 8px; align-items: center;">
                            <p style="margin-right: 5px;">Exportar</p>
                            <Boton @click="exportarPDF" tipo="primario2">PDF</Boton> 
                            <Boton @click="exportarCSV" tipo="secundarioBorde">CSV</Boton>
                        </div>

                        <!-- <GraficoGastosPorMes ref="graficoGastosPorMes" :data="getData2()" :options="getOptions2()" /> -->
                    </div>

                    <div v-if="grupoSeleccionado.total > 0" style="margin-top: 15px; display: flex; gap: 15px;">
                        <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                            <h3 style="margin-bottom: 0; padding-bottom: 0;">{{ vistaPDF ? 'Tus gastos por mes' : 'Tus gastos por mes en ' + grupoSeleccionado.nombre }}
                            </h3>
                            <GraficoGastosPorMes style="width: 350px" v-show="tieneGastos" ref="graficoGastosPorMes" :data="getData2()" :options="getOptions2()" />
                            <div v-show="!tieneGastos" style="width: 630px; height: 230px; text-align: center">
                                <p style="margin-top: 100px">No ha realizado gastos en {{ year }}</p>
                            </div>
                            <!-- <GraficoUsuariosPorDia ref="graficoUsuariosPorDia" :data="getData3()" :options="getOptions3()" /> -->
                            <div style="display:flex; gap: 15px; align-items: center;">
                                Año
                                <v-card elevation="0" style="width: 100px;">
                                    <v-text-field variant="outlined" density="compact"
                                        style="display:inline; padding: 0!important;" class="pa-2" type="number" min="0"
                                        hide-details :model-value="year" v-model="year">
                                    </v-text-field>
                                </v-card>
                            </div>

                        </div>
                        <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                            <h3 style="margin-bottom: 0; padding-bottom: 0;">Categorias</h3>
                            <GraficoCategoriaMonto ref="graficoCategoriaMonto" :data="getData3()"
                                :options="getOptions3()" />
                        </div>
                    </div>
                    <div v-if="grupoSeleccionado.avatarConMasDeuda.saldo > 0" :style="vistaPDF ? 'margin-top:100px': ''" style="display:flex; justify-content: center;  margin-bottom: 50px;">
                        <div id="wrapper" style="margin-top: 15px;">
                            <div id="contenedorGrafico"
                                style="display: flex; flex-direction: column; align-items: center; width: 320px;">
                                <h3 style="margin-bottom: 0; padding-bottom: 0;">Mayor deudor</h3>


                                <div
                                    style="margin-top: 10px; display:flex; flex-direction: column; align-items: center; gap: 4px;">
                                    <!-- Sin imagen -->
                                    <img v-if="grupoSeleccionado.avatarConMasDeuda.imagen == null"
                                        src="/src/assets/svg/User2.svg" class="botonNavBar"
                                        style="width: 140px;margin-top: 0px;" />
                                    <!-- Con imagen -->
                                    <img v-else :src="`data:image/png;base64,${grupoSeleccionado.avatarConMasDeuda.imagen}`"
                                        class="roundedd">

                                    <h3>{{ grupoSeleccionado.avatarConMasDeuda.nombre }}</h3>

                                    <img style="width:55px; margin-top: 9px; margin-bottom: 79px;"
                                        src="/src/assets/svg/Trofeo.svg">

                                    
                                </div>
                            </div>
                            <div id="overlay">
                                <div style="position:relative; display:flex; flex-direction: column; align-items: center;">
                                    <img style="width:600px" src="/src/assets/svg/Decoracion2.svg">
                                <div style="font-size: 30px; color:white; position: absolute; bottom: 10px; print-color-adjust: exact;">
                                        {{signoDinero(grupoSeleccionado.moneda)}}{{ grupoSeleccionado.avatarConMasDeuda.saldo }}
                                    </div>
                                </div>
                                
                            </div>

                           
                            
                        </div>
                    </div>
                </div>

                </Transition>

            </div>
            <div v-else style="color:gray">
                Crea un grupo y realiza gastos para poder visualizar estadisticas
            </div>


        </div>





    </div>
</template>
<style scoped src="../assets/css/formulario.css"/>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 100px;
}

.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 165px;
    width: 165px;
    outline: 1px solid rgb(111, 111, 111);
    margin-top: 15px;
    margin-left: 15px;
    margin-bottom: 15px;
    margin-right: 2px;
}

#contenedorGrafico {
    border-style: solid;
    border-width: 1px;
    border-color: rgb(150, 150, 150);
    border-radius: 10px;
    padding: 10px;
    padding-right: 20px;
    padding-left: 20px;
    box-shadow: 0px 1px 2px 1px rgba(0, 0, 0, 0.2);
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(127, 127, 127);

}

#overlay{
    position: absolute;
    
    bottom: 12px
}

#wrapper{
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
}


</style>