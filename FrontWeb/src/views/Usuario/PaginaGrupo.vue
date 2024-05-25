<script>
import Boton from '../../components/Botones/Boton.vue'
import { RouterLink } from 'vue-router'
import axios from 'axios';
import GraficoSaldos from '../../components/Graficos/GraficoSaldos.vue';
import TabGastos from '../../components/TabsGrupo/TabGastos.vue';
import TabPagos from '../../components/TabsGrupo/TabPagos.vue';
import VerIntegrantes from '../../components/Modales/VerIntegrantes.vue';
import NuevoGasto from '../../components/Modales/NuevoGasto.vue'
import TabReporte from '../../components/TabsGrupo/TabReporte.vue';
import NuevoIntegrante from '../../components/Modales/NuevoIntegrante.vue';
import LiquidarDeudasModal from '../../components/Modales/LiquidarDeudasModal.vue';
import InfoAvatar from '../../components/Modales/InfoAvatar.vue';
import EliminarGrupoDialogo from '../../components/Dialogos/EliminarGrupoDialogo.vue'
import EditarGrupoModal from '../../components/Modales/EditarGrupoModal.vue';

export default {
    name: 'paginaGrupo',
    components: {
        Boton,
        GraficoSaldos,
        TabGastos,
        TabPagos,
        TabReporte,
        VerIntegrantes,
        NuevoGasto,
        NuevoIntegrante,
        LiquidarDeudasModal,
        InfoAvatar,
        EliminarGrupoDialogo,
        EditarGrupoModal
    },
    data() {
        return {
            imagen: null,
            tipoGrupo: '',
            nombre: '',
            integrantes: [

            ],
            pendiente: false,
            tab: 0,
            moneda: '',
            saldoUsuario: 0,
            categoriasGrupo: [],
            pagos: [],
            gastos: [],
            reporteElementos: [],
            cargando: true
        }
    },
    methods: {
        getIconoTipoGrupo() {
            let tipo = this.tipoGrupo
            switch (tipo) {
                case "VIAJE": return 'mdi-airplane'
                case "CASA": return 'mdi-home'
                case "EVENTO": return 'mdi-calendar'
                case "PAREJA": return 'mdi-heart'
                case "PROYECTO": return 'mdi-clipboard-outline'
                case "OTRO": return 'mdi-account-group'
                default: return 'mdi-account-group'
            }
        },
        async fetchDatosGrupo() {
            //convertir tipoGrupo a lowercase y guardarlo den tipoGrupo

            const url = import.meta.env.VITE_API + "/api/grupos/usf/obtenerInfoGrupo/" + this.$route.params.id
            const response = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    // debugger
                    //
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                        if(response.data.mensaje == "El usuario no pertenece al grupo."){
                            this.$router.push('/SinAcceso')
                        }
                    } else {
                        if(response.data.nombre == null){
                            this.$router.push('/PaginaNoEncontrada')
                        }

                        if (response.data.imagen && response.data.imagen != 'null') {
                            this.imagen = response.data.imagen
                        } else {
                            this.imagen = null
                        }
                        this.nombre = response.data.nombre
                        this.tipoGrupo = response.data.tipoGrupo
                        this.moneda = response.data.moneda
                        this.saldoUsuario = response.data.saldoLocal
                        //this.categoriasGrupo = response.data.categoriasGrupo
                        this.pagos = response.data.pagos.reverse()
                        this.gastos = response.data.gastosCompartidos.reverse()
                        this.integrantes = response.data.participantes
                        if (this.integrantes.some(i => i.saldoLocal != 0)) {
                            this.pendiente = true
                        } else {
                            this.pendiente = false
                        }
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));


            const url2 = import.meta.env.VITE_API + "/api/auditoria/usr/grupo/" + this.$route.params.id
            const response2 = await axios.get(url2,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    if (response.data.estatus != null && response.data.estatus == false) {//Error
                        this.errorPostSubmit = response.data.mensaje
                    } else {
                        this.reporteElementos = response.data.reverse()
                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

            const url3 = import.meta.env.VITE_API + "/api/categorias/usf/getByGrupo/" + this.$route.params.id
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
                        this.categoriasGrupo = response.data
                    }

                }.bind(this))
                .catch(function (error) {
                    //  debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));


            this.$emit("terminoCargar")
        },
        getData() {
            let nombres = []
            let saldos = []
            let colores = []
            let coloresBorde = []
            const r = document.querySelector(':root')
            const rs = getComputedStyle(r)
            const colorPrimario = rs.getPropertyValue('--color-primario')

            this.integrantes.forEach(integrante => {
                if (!integrante.esUsuarioActual) nombres.push(integrante.nombre)
                else nombres.push("Yo")
                saldos.push(integrante.saldoLocal.toFixed(2))
                colores.push(integrante.saldoLocal >= 0 ? "rgb(255, 255, 255)" : colorPrimario)
                coloresBorde.push(integrante.saldoLocal >= 0 ? "gray" : "rgb(232, 232, 232)")
            })
            const obj = {
                labels: nombres,
                datasets: [{
                    label: 'aassadas',
                    data: saldos,
                    backgroundColor: colores,
                    borderColor: coloresBorde,
                    borderWidth: 1
                }]
            }
            return obj
        },
        getOptions() {

            const r = document.querySelector(':root')
            const rs = getComputedStyle(r)
            const colorPrimario = rs.getPropertyValue('--color-primario')

            const obj = {
                responsive: true,
                barThickness: 50,
                maintainAspectRatio: false,
                animation: {
                    delay: 1000,
                },
                layout: {
                    padding: {
                        left: 0,
                        right: 0,
                        top: 7,
                        bottom: this.pendiente ? 4 : 46
                    }
                },
                interaction: {
                    // intersect: true,
                    // axis: 'x'
                },
                plugins: {
                    // title:{ display: true, text: 'Saldos'},
                    legend: { display: false },
                    datalabels: {
                        backgroundColor: 'rgb(255, 255, 255)',
                        borderRadius: 50,
                        borderColor: 'rgb(0, 0, 0)',
                        borderWidth: 1,
                        font: { family: "Lexend" },
                        anchor: function (context) {
                            var index = context.dataIndex;
                            var value = context.dataset.data[index]
                            return value < 0 ? 'end' : 'start'
                        },
                        align: function (context) {
                            var index = context.dataIndex;
                            var value = context.dataset.data[index]
                            return value < 0 ? 'top' : 'bottom'
                        },
                        labels: {
                            title: {
                                backgroundColor: colorPrimario,
                                color: 'rgb(255, 255, 255)',
                                borderWidth: 0,
                                offset: function (context) {
                                    var index = context.dataIndex;
                                    var value = context.dataset.data[index]
                                    return value < 0 ? 28 : 4
                                },
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => (" " + ctx.chart.data.labels[ctx.dataIndex] + " ")
                            },
                            value: {
                                offset: function (context) {
                                    var index = context.dataIndex;
                                    var value = context.dataset.data[index]
                                    return value < 0 ? 4 : 27
                                },
                                font: {
                                    lineHeight: 1
                                },
                                formatter: (val, ctx) => {
                                    if (val >= 0) {
                                        return " $" + Math.abs(val) + " "
                                    } else {
                                        return " -$" + Math.abs(val) + "  "
                                    }
                                }
                            }
                        },

                    },
                    annotation: {
                        annotations: {
                            line1: {
                                type: 'line',
                                yMin: 0,
                                yMax: 0,
                                borderColor: 'rgb(0, 0, 0)',
                                borderWidth: 1,
                            }
                        }
                    },
                    tooltip: {
                        enabled: false
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        },
                        border: {
                            display: false,
                        },
                        ticks: {
                            display: false
                        },
                    },
                    y: {
                        grid: {
                            tickLength: 0
                        },
                        ticks: {
                            display: false
                        },
                        border: {
                            display: true,
                            width: 1
                        }
                    }

                }
            }

            return obj
        },
        verIntegrantes() {
            
            this.$refs.verIntegrantes.toggleDialog(true)

        },
        nuevoGasto() {
            
            this.$refs.nuevoGrupo.toggleDialog(true)
        },
        nuevoIntegrante() {
            
            this.$refs.nuevoIntegrante.toggleDialog(true)
        },
        liquidarDeudas() {
            
            this.$refs.liquidarDeudasModal.toggleDialog(true)
        },
        infoIntegrante(integrante) {
            this.$refs.infoAvatar.setAvatar(integrante)
            this.$refs.infoAvatar.toggleDialog(true)
        },
        infoIntegranteIndex(integranteIndex) {
            const integ = this.integrantes[integranteIndex]
            this.$refs.infoAvatar.setAvatar(integ)
            this.$refs.infoAvatar.toggleDialog(true)
        },
        eliminarGrupo() {
            
            this.$refs.eliminarGrupo.setearGrupoId(this.$route.params.id)
            this.$refs.eliminarGrupo.toggleDialog(true)
        },
        graficoClick(ev){
            
            this.$refs.graficoSaldos.handleClick(ev)
        },
        editarGrupo(){
            this.$refs.editarGrupoModal.toggleDialog(true)
        },
        oo(mensaje){
            
        },
        refetchWebSocket(idGrupo){

            this.fetchDatosGrupo()
        }
    },
    computed: {
        tipoGrupoCapitalizado() {
            return this.tipoGrupo.charAt(0).toUpperCase() + this.tipoGrupo.slice(1).toLowerCase()
        }
    },
    mounted() {
        this.fetchDatosGrupo()
        

    }
}

</script>





<template>
    <!-- Esta es la pagina del grupo con id {{ $route.params.id }}, que se tiene que verificar si existe, y si tienes permiso para verla -->
    <NuevoGasto ref="nuevoGrupo" @refetch="fetchDatosGrupo" :integrantes="integrantes" :categoriasGrupo="categoriasGrupo"
        :grupoId="$route.params.id" />
    <VerIntegrantes ref="verIntegrantes" :grupo="nombre" :integrantes="integrantes" />
    <NuevoIntegrante ref="nuevoIntegrante" :grupoId="$route.params.id" @refetch="fetchDatosGrupo" />
    <LiquidarDeudasModal ref="liquidarDeudasModal" :grupoId="$route.params.id" :moneda="moneda"
        @refetch="fetchDatosGrupo" />
    <InfoAvatar ref="infoAvatar" :grupoId="$route.params.id" @refetch="fetchDatosGrupo" />
    <EliminarGrupoDialogo ref="eliminarGrupo" :grupoId="$route.params.id" />
    <EditarGrupoModal ref="editarGrupoModal" :info-grupo="{id: $route.params.id, nombre: nombre, tipoGrupo: tipoGrupo, imagen: imagen}" @refetch="fetchDatosGrupo"/>
    <v-navigation-drawer location="right" permanent elevation="2" >
        <div style="padding: 15px; padding-top: 28px;">
            <div style="display: flex; flex-direction: column; gap: 6px; justify-content: center;">
                <Boton @click="nuevoGasto" style="width:100%" tipo="primario2" icono-custom="iconoGasto">Nuevo gasto</Boton>
                <Boton @click="liquidarDeudas" style="width:100%" tipo="primario2" icono-custom="iconoLiquidarDeudas">
                    Liquidar deudas</Boton>
            </div>
            <div style="display: flex; flex-direction: column; gap: 6px; margin-top: 15px;">
                <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;"
                    @click="nuevoIntegrante()">
                    <v-icon style="margin-right: 4px;">
                        mdi-account-plus
                    </v-icon>
                    <div style="white-space: nowrap;">
                        Nuevo integrante
                    </div>
                </div>
                <!-- <RouterLink id="routerlink" to="/">
                    <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;" @click="editar(i)">
                        <v-icon style="margin-right: 4px;">
                            mdi-export-variant
                        </v-icon>
                        <div style="white-space: nowrap;">
                            Exportar
                        </div>
                    </div>
                </RouterLink> -->
                <!-- <RouterLink id="routerlink" to="/">
                    <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;" @click="editar(i)">
                        <v-icon style="margin-right: 4px;">
                            mdi-format-list-bulleted
                        </v-icon>
                        <div style="white-space: nowrap;">
                            Ver reporte histórico
                        </div>
                    </div>
                </RouterLink> -->
                <!-- <RouterLink id="routerlink" to="/">-->
                <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;"
                    @click="editarGrupo()">
                    <v-icon style="margin-right: 4px;">
                        mdi-pencil
                    </v-icon>
                    <div style="white-space: nowrap;">
                        Editar grupo
                    </div>
                </div>
                <div style="display: flex; cursor: pointer; margin-right: 10px; margin-left: 10px;"
                    @click="eliminarGrupo()">
                    <v-icon style="margin-right: 4px;">
                        mdi-trash-can-outline
                    </v-icon>
                    <div style="white-space: nowrap;">
                        Eliminar grupo
                    </div>
                </div>
                <!--</RouterLink>-->
            </div>
        </div>
    </v-navigation-drawer>
    <div id="contenedor1">
        <div style="display: flex; justify-content: space-between;">
            <div style="display: flex; padding-bottom: 10px;">
                <div>
                    <!-- Sin imagen -->
                    <img v-if="imagen == null || imagen ==''" src='/src/assets/images/Grupo.png' style="width: 140px;margin-top: 0px;"
                        class="squared" />
                    <!-- Con imagen -->
                    <img v-else :src="`data:image/png;base64,${imagen}`" class="squared">
                </div>
                <div style="margin-left: 20px;">
                    <v-chip size="small" style="font-size: 11px; background-color: var(--color-primario); color: white;">
                        <template v-slot:prepend>
                            <v-icon style="margin-right: 5px;">{{ getIconoTipoGrupo() }}</v-icon>
                        </template>
                        {{ tipoGrupoCapitalizado }}
                    </v-chip>
                    <div style="display: flex;gap: 5px; flex-direction: column;">
                        <h2>{{ nombre }}</h2>
                        <div style="display: flex; gap: 6px;">
                            <!-- <p style="margin-right: 8px;">Con: </p> -->
                            <template v-for="(integrante, i) in integrantes">
                                <v-chip @click="infoIntegrante(integrante)" v-if="!integrante.esUsuarioActual && i < 7"
                                    :key="i" style="font-size: 11px;" variant="outlined"
                                    :style="integrante.estaVinculado ? 'padding-left: 4px;' : ''">
                                    <template v-slot:prepend v-if="integrante.estaVinculado">
                                        <!-- Sin imagen -->
                                        <img v-if="integrante.imagen == null || integrante.imagen ==''" src="/src/assets/svg/User2.svg"
                                            style="width: 25px;margin-top: 0px; margin-right: 8px;" />
                                        <!-- Con imagen -->
                                        <img v-else :src="`data:image/png;base64,${integrante.imagen}`" class="roundedd"
                                            style="margin-right: 8px;">
                                    </template>
                                    {{ integrante.nombre }}
                                </v-chip>
                            </template>
                            <template v-if="integrantes.length > 6">
                                <v-chip style="font-size: 11px; cursor: pointer;" variant="outlined"
                                    @click="verIntegrantes">
                                    <v-icon>mdi-dots-horizontal</v-icon>
                                </v-chip>
                            </template>
                        </div>
                        <div v-if="pendiente" style="display: flex; align-items: center; margin-top: 5px;">
                            <v-icon style="font-size: 15px;">mdi-alert-circle-outline</v-icon>
                            <p style="font-size: 10px; margin-left: 4px;">PENDIENTE</p>
                        </div>

                    </div>
                </div>
            </div>
            <div style="width: 440px; display: flex; gap: 15px;">



            </div>
        </div>
        <!-- Fin de header -->
        <v-divider />
        <v-tabs fixed-tabs v-model="tab">
            <v-tab style="font-size: 16px;text-transform: unset !important;" :ripple="false">
                Saldos
            </v-tab>
            <v-tab style="font-size: 16px;text-transform: unset !important;" :ripple="false">
                Gastos
            </v-tab>
            <v-tab style="font-size: 16px;text-transform: unset !important;" :ripple="false">
                Pagos
            </v-tab>
            <v-tab style="font-size: 16px;text-transform: unset !important;" :ripple="false">
                Actividad
            </v-tab>

        </v-tabs>
        <v-window v-model="tab">
            <v-window-item value="0" :key="tab">



                <GraficoSaldos @mostrarInfoIndex="infoIntegranteIndex" ref="graficoSaldos" @click="graficoClick" style="margin-top:20px" :data="getData()" :options="getOptions()" />

            </v-window-item>
            <v-window-item value="1" key="1">
                <TabGastos @refetch="fetchDatosGrupo" @mostrarInfo="infoIntegrante" :todos-participantes="integrantes" :gastos="gastos"/>
            </v-window-item>
            <v-window-item value="2" key="2">
                <TabPagos @refetch="fetchDatosGrupo" @mostrarInfo="infoIntegrante" :todos-participantes="integrantes" :pagos="pagos"/>
            </v-window-item>
            <v-window-item value="3" key="3">
                <TabReporte :auditorias="reporteElementos" />
            </v-window-item>
        </v-window>
    </div>
</template>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
    margin-bottom: 180px;
}

.squared {
    object-fit: cover;
    border-radius: 10%;
    height: 140px;
    width: 140px;
    outline: 1px solid rgb(111, 111, 111);
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 23px;
    width: 23px;
    outline: 1px solid rgb(111, 111, 111);

}

.v-enter-active,
.v-leave-active {
    transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
    opacity: 0;
}

#routerlink {
    text-decoration: none;
    color: black;
}</style>