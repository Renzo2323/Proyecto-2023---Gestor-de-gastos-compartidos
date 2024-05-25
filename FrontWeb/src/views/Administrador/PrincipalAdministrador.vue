<script>
import GraficoCategoriaMonto from '../../components/Graficos/GraficoCategoriaMonto.vue';
import GraficoGastosPorMes from '../../components/Graficos/GraficoGastosPorMes.vue';
import GraficoUsuariosPorDia from '../../components/Graficos/GraficoUsuariosPorDia.vue';
import GraficoActividadPorMes from '../../components/Graficos/GraficoActividadPorMes.vue';
import Boton from '../../components/Botones/Boton.vue'
import axios from 'axios';
import { DateTime } from 'luxon'
// import * as JSZip from 'jszip'
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
            categorias: [

            ],
            totalGastosPorMes: [1500, 400, 550, 3200, 2900, 2000, 1400, 200, 10, 30, 0, 0],
            usuariosRegistradosPorDia: [

            ],
            gruposCreadosPorDia: [

            ],
            transaccionesPorDia: [],
            vistaPDF: false
        }
    },
    methods: {
        getData1() {
            let nombres = []
            let montosTotales = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            this.categorias.forEach(categoria => {

                nombres.push(categoria.categoria)
                montosTotales.push(categoria.cantidad)

            })
            const obj = {
                labels: nombres,
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
                                text: `${chart.data.labels[i]} ${data}`,
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
                                    // return " $" + Math.abs(val) + " "
                                    return " " + val + " "

                                }
                            }
                        }
                    },
                },

            }
        },
        getData2() {
            let meses = ['En', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ag', 'Sep', 'Oct', 'Nov', 'Dic']
            let montosTotales = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            const obj = {
                labels: meses,
                datasets: [{
                    label: 'aassadas',
                    data: this.totalGastosPorMes,
                    backgroundColor: coloresRandom,
                    // borderColor: 'black',
                    borderWidth: 1
                }]
            }
            
            return obj
        },
        getOptions2() {
            return {
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
                            title: {
                                backgroundColor: 'rgb(255, 61, 96)',
                                color: 'rgb(255, 255, 255)',
                                borderWidth: 1,
                                borderColor: '#8f183e',
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    switch (ctx.chart.data.labels[ctx.dataIndex]) {
                                        case "En": return 'Enero'
                                        case "Feb": return 'Febrero'
                                        case "Mar": return 'Marzo'
                                        case "Abr": return 'Abril'
                                        case "May": return 'Mayo'
                                        case "Jun": return 'Junio'
                                        case "Jul": return 'Julio'
                                        case "Ag": return 'Agosto'
                                        case "Sep": return 'Septiembre'
                                        case "Oct": return 'Octubre'
                                        case "Nov": return 'Noviembre'
                                        case "Dic": return 'Diciembre'
                                    }
                                }
                            },
                            value: {
                                offset: 27,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    return " $" + Math.abs(val) + " "

                                }
                            }
                        }
                    },
                },
            }
        },
        getDates(fechas) {
            let datetimes = []
            fechas.forEach(f => {
                let year = f.split('-')[0]
                // if (year != this.year) {
                //     return
                // }
                let month = f.split('-')[1]
                let day = f.split('-')[2]
                datetimes.push(DateTime.fromObject({ year: year, month: month, day: day }))
            })
            if (datetimes.length > 0) {
                this.tieneGastos = true
            } else {
                this.tieneGastos = false
            }
            //let datetimes = [DateTime.fromObject({ year: 2000, month: 1, day: 1 }), DateTime.fromObject({ year: 2000, month: 3, day: 8 }), DateTime.fromObject({ year: 2000, month: 7, day: 8 }), DateTime.fromObject({ year: 2000, month: 8, day: 8 })];
            return datetimes
        },
        getDate(fecha) {
            let year = fecha.split('-')[0]
            let month = fecha.split('-')[1]
            let day = fecha.split('-')[2]
            return DateTime.fromObject({ year: year, month: month, day: day })
        },
        getData3() {
            let fechas = []
            let fechasGrupos = []
            let cantidades = []
            let cantidadesGrupos = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#d1477f', '#d147b5', '#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b']
            let coloresRandom = this.shuffle(coloresEstaticos)
            let paresUsuario = []
            let paresGrupo = []
            //usuariosRegistradosPorDia

            this.usuariosRegistradosPorDia.forEach(urpd => {

                fechas.push(urpd.fecha)
                cantidades.push(urpd.cantidad)
                paresUsuario.push({
                    x: this.getDate(urpd.fecha),
                    y: urpd.cantidad
                })

            })
            let aa = this.getDates(fechas)

            this.gruposCreadosPorDia.forEach(grpd => {

                fechasGrupos.push(grpd.fecha)
                cantidadesGrupos.push(grpd.cantidad)
                paresGrupo.push({
                    x: this.getDate(grpd.fecha),
                    y: grpd.cantidad
                })

            })

            let bb = this.getDates(fechasGrupos)





            const obj = {
                //labels: aa,
                datasets: [{
                    label: 'Usuarios registrados',
                    data: paresUsuario,

                    borderColor: 'rgb(255, 61, 96)',
                    borderWidth: 3,
                    lineTension: 0.4

                },
                {
                    label: 'Grupos creados',
                    data: paresGrupo,

                    borderColor: '#53c9b8',
                    borderWidth: 3,
                    lineTension: 0.4

                }]
            }
            
            return obj
        },
        getOptions3() {
            return {
                layout: {
                    padding: {
                        left: 8,
                        right: 41,
                        top: 44,
                        bottom: 0
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'day',
                            displayFormats: {
                                day: 'DD'
                            }
                        },
                        ticks: {
                            // callback: function (val, index) {
                            //     return this.getLabelForValue(val).split('-')[2];
                            // },
                            source: 'auto'
                        },
                    },
                    y: {
                        ticks: {
                            precision: 0
                        },
                        beginAtZero: true
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
                        align: 'top',
                        labels: {
                            // title: {
                            //     backgroundColor: 'rgb(255, 61, 96)',
                            //     color: 'rgb(255, 255, 255)',
                            //     borderWidth: 1,
                            //     borderColor: '#8f183e',
                            //     offset: 34,
                            //     font: {
                            //         lineHeight: 1
                            //     },
                            //     formatter: (val, ctx) => {
                            //         return " " + ctx.chart.data.labels[ctx.dataIndex] + " "

                            //     }
                            // },
                            value: {
                                offset: 10,
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    return " " + val.y + " "

                                }
                            },
                        }
                    },
                },
            }
        },
        getData4() {
            // let meses = ['En', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ag', 'Sep', 'Oct', 'Nov', 'Dic']
            let cantidades = []
            let colores = []
            let coloresBorde = []
            let coloresEstaticos = ['#bc54cc', '#9853c9', '#7653c9', '#5553c9', '#536cc9', '#538ec9', '#53a2c9', '#53c0c9', '#53c9b8', '#53c9a0', '#53c97e', '#5db064', '#72b05d', '#8db357', '#a5bd44', '#dee04a', '#dbba40', '#d69936', '#d67836', '#d45d2f', '#cf4836', '#cc3f5b', '#d1477f', '#d147b5',]
            let coloresRandom = this.shuffle(coloresEstaticos)
            let fechas = []

            this.transaccionesPorDia.forEach(t => {

                fechas.push(t.fecha)
                cantidades.push(t.cantidad)

            })
            let aa = this.getDates(fechas)
            


            const obj = {
                labels: aa,
                datasets: [{
                    label: 'aassadas',
                    data: cantidades,
                    borderColor: 'rgb(255, 61, 96)',
                    borderWidth: 3,
                    lineTension: 0.4
                }]
            }
            
            return obj
        },
        getOptions4() {
            return {
                layout: {
                    padding: {
                        left: 8,
                        right: 41,
                        top: 44,
                        bottom: 0
                    }
                },
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'day',
                            displayFormats: {
                                day: 'DD'
                            }
                        },
                        ticks: {
                            // callback: function (val, index) {
                            //     return this.getLabelForValue(val).split('/')[0];
                            // },
                            source: 'auto'
                        },
                    },
                    y: {
                        ticks: {
                            // display: false,
                            precision: 0,
                            
                        },
                        beginAtZero: true
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
                        align: 'top',
                        labels: {
                            value: {
                                formatter: (val, ctx) => {
                                return " " + val + " "
                                }
                            }
                            // title: {
                            //     backgroundColor: 'rgb(255, 61, 96)',
                            //     color: 'rgb(255, 255, 255)',
                            //     borderWidth: 1,
                            //     borderColor: '#8f183e',
                            //     offset: 11,
                            //     font: {
                            //         lineHeight: 1
                            //     },
                            //     formatter: (val, ctx) => {
                            //         switch (ctx.chart.data.labels[ctx.dataIndex]) {
                            //             case "En": return 'Enero'
                            //             case "Feb": return 'Febrero'
                            //             case "Mar": return 'Marzo'
                            //             case "Abr": return 'Abril'
                            //             case "May": return 'Mayo'
                            //             case "Jun": return 'Junio'
                            //             case "Jul": return 'Julio'
                            //             case "Ag": return 'Agosto'
                            //             case "Sep": return 'Septiembre'
                            //             case "Oct": return 'Octubre'
                            //             case "Nov": return 'Noviembre'
                            //             case "Dic": return 'Diciembre'
                            //         }
                            //     }
                            // },
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
        async fetchReportes() {
            const url = import.meta.env.VITE_API + "/api/reportes/adm/RegistrosPorDia"
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
                        this.usuariosRegistradosPorDia = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

            const url2 = import.meta.env.VITE_API + "/api/reportes/adm/GruposPorDia"
            const response2 = await axios.get(url2,
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
                        this.gruposCreadosPorDia = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

            const url3 = import.meta.env.VITE_API + "/api/reportes/adm/CantGastosPorCategoria"
            const response3 = await axios.get(url3,
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
                        this.categorias = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));


            const url4 = import.meta.env.VITE_API + "/api/reportes/adm/TransaccionesPorDia"
            const response4 = await axios.get(url4,
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
                        this.transaccionesPorDia = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        },
        exportarPDF(){
            
            this.$emit('toggleLeftBar', false)
            this.$emit('toggleNavBar', false)
            this.vistaPDF = true

            setTimeout(function() { print(); }, 1000);
            
        },
        handleBeforePrint(event){
            
            // this.$emit('toggleLeftBar', false)
            // this.$emit('toggleNavBar', false)
        },
        handleAfterPrint(event){
            
            this.$emit('toggleLeftBar', true)
            this.$emit('toggleNavBar', true)
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
            zip.file('GastosPorCategorias.csv', this.convertToCSV(this.categorias) ).
            file('UsuariosRegistradosPorDia.csv', this.convertToCSV(this.usuariosRegistradosPorDia)).
            file('GruposCreadosPorDia.csv', this.convertToCSV(this.gruposCreadosPorDia)).
            file('TransaccionesPorDia.csv', this.convertToCSV(this.transaccionesPorDia))
            
            let promise = zip.generateAsync({type : "blob"})
            .then(function(blob){
                const fecha = (new Date()).toLocaleDateString();
                const nombre = 'EstadisticasGlobales-' + fecha + '.zip' 
                saveAs(blob, nombre)
            })
            
        }

    },
    mounted() {
        this.fetchReportes()
        this.$emit("terminoCargar")
    },
    created(){
        window.addEventListener("beforeprint", this.handleBeforePrint)

        window.addEventListener("afterprint", this.handleAfterPrint)
    },
    unmounted(){
        window.removeEventListener("beforeprint", this.handleBeforePrint)
        window.removeEventListener("afterprint", this.handleAfterPrint)
    }
}


// window.addEventListener("beforeprint", (event) =>{
//     // $emit('toggleLeftBar', false)
//     // $emit('toggleNavBar', false)
//     
    
//     
// })

// window.addEventListener("afterprint", (event) =>{
//     // $emit('toggleLeftBar', true)
//     // $emit('toggleNavBar', true)
    
//     
// })



</script>

<template>
    <!-- <p>Pagina principal de Administrador</p> -->
    <div id="contenedor1">
        <div>
            <div style="display:flex; justify-content: space-between">
                <h2>Estadísticas globales</h2>
                <div v-if="!vistaPDF" style="display:flex; gap: 8px; align-items: center;">
                    <p style="margin-right: 5px;">Exportar</p>
                    <Boton @click="exportarPDF" tipo="primario2">PDF</Boton> 
                    <Boton @click="exportarCSV" tipo="secundarioBorde">CSV</Boton>
                </div>
            </div>
            
            <div style="display:flex; gap: 15px; margin-top: 15px;">
                <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                    <h3 style="margin-bottom: 0; padding-bottom: 0;">Gastos por categoría</h3>
                    <GraficoCategoriaMonto style="display: flex; justify-content: center;" :style="vistaPDF ? 'width:670px;' : 'width:870px;'" ref="graficoCategoriaMonto" :data="getData1()" :options="getOptions1()" />
                </div>
                <!-- <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                    <h3 style="margin-bottom: 0; padding-bottom: 0;">Grafica incompleta</h3>
                    <GraficoGastosPorMes width=580 height=280 ref="graficoGastosPorMes" :data="getData2()"
                        :options="getOptions2()" />
                </div> -->
            </div>
            <div style="margin-top: 15px;">
                <div id="contenedorGrafico" :style="vistaPDF ? 'width:710px;' : 'width:910px;'" style="display: flex; flex-direction: column; align-items: center;">
                    <h3 style="margin-bottom: 0; padding-bottom: 0;">Usuarios y grupos por dia</h3>
                    <div style="display:flex; gap:5px; font-size: 14px; align-items: center;margin-top: 8px;">
                        <div :style="vistaPDF ? 'margin-right:15px' : ''">
                            {{ vistaPDF ? 'Usuarios registrados: Rojo   ' : 'Usuarios registrados'}}
                        </div>
                        
                        <div v-if="!vistaPDF"
                            style="background-color:rgb(255, 61, 96); width: 18px; height: 18px; border-radius: 3px; margin-right: 8px;" />
                        <div>
                            {{ vistaPDF ? 'Grupos creados: Azul' : 'Grupos creados'}}
                        </div>
                        
                        <div v-if="!vistaPDF" style="background-color:#53c9b8; width: 18px; height: 18px; border-radius: 3px;" />

                    </div>
                    <GraficoUsuariosPorDia  ref="graficoUsuariosPorDia" :data="getData3()" :options="getOptions3()" />
                </div>
            </div>
            <div :style="vistaPDF ? 'margin-top: 500px;' : 'margin-top: 15px'">
                <div id="contenedorGrafico" style="display: flex; flex-direction: column; align-items: center;">
                    <h3 style="margin-bottom: 0; padding-bottom: 0;">Transacciones por dia</h3>
                    <GraficoActividadPorMes :style="vistaPDF ? 'width:670px;' : 'width:870px;'" ref="graficoActividadPorMes" :data="getData4()" :options="getOptions4()" />
                </div>
            </div>
        </div>


    </div>
</template>


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
</style>