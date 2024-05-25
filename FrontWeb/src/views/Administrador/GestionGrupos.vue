<script>
import Boton from '../../components/Botones/Boton.vue';

import axios from 'axios';
import ResumenGrupo from '../../components/Modales/ResumenGrupo.vue';

export default {
    name: 'gestionGrupos',
    components: {
        Boton,
        ResumenGrupo
    },
    data() {
        return {
            itemsPerPage: 5,
            page: 1,
            headers: [
                { title: '', align: 'end', key: 'imagen', sortable: false, width: '5%' },
                {
                    title: 'Nombre',
                    align: 'start',
                    key: 'nombre',
                },
                { title: 'Id', align: 'end', key: 'id', align: 'left' },
                { title: 'TipoGrupo', align: 'end', key: 'tipo', align: 'left' },
                { title: 'Moneda', align: 'end', key: 'moneda', align: 'left' },
                { title: '', align: 'end', key: 'menu', sortable: false },

            ],
            grupos: [],
            gruposFiltrados: [],
            drawer: null,
            selectTipo: "todos",
            cargando: false,
            busqueda: '',
            falloFetch: false
        }
    },
    methods: {
        aplicarFiltros() {
            let resultado = this.grupos

            if(this.selectTipo != "todos"){
                resultado = resultado.filter((grupo) => grupo.tipo == this.selectTipo)
            }


            this.gruposFiltrados = resultado

        },
        menuOpcion(opcion, item) {
            
            
            if (opcion == 'Ver resúmen de actividad') {
                this.mostrarResumen(item.id, item.nombre)
            }
        },
        async fetchGrupos() {
            this.cargando = true
            this.grupos = []
            this.gruposFiltrados = []
            const response = await axios.get(import.meta.env.VITE_API + "/api/grupos/adm", 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    response.data.forEach(grupo => {
                        this.grupos.push({
                            id: grupo.id,
                            nombre: grupo.nombre,
                            tipo: grupo.tipoGrupo.toLowerCase(),
                            moneda: grupo.moneda,
                            imagen: grupo.imagen
                        })
                    });
                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
            this.aplicarFiltros()
            this.cargando = false
        },
        mostrarResumen(id, nombre){
            this.$refs.resumenGrupo.setGrupo(id, nombre)
            this.$refs.resumenGrupo.toggleDialog(true)
        }
    },
    async mounted() {
        this.$emit("terminoCargar")
        this.fetchGrupos()
    },
    computed: {
        pageCount() {
            return Math.ceil(this.gruposFiltrados.length / this.itemsPerPage)
        },
        mensajeSinGrupos() {
            if (this.falloFetch) {
                return 'No se pudieron traer los grupos de la base de datos'
            }

            if (this.grupos.length > 0) {
                return 'Ningun grupo coincide con los parametros'
            } else {
                return 'No hay grupos en el sistema'
            }

        }
    },
}
</script>



<template>
    <ResumenGrupo ref="resumenGrupo"/>
    <div id="contenedor1">
        <v-navigation-drawer location="right" v-model="drawer" temporary>
            <v-list-item>
                <template v-slot:append>
                    <v-icon @click="drawer = !drawer" icon="mdi-window-close"></v-icon>
                </template>
                <v-list-item-title class="textoVuetify">Filtros</v-list-item-title>
            </v-list-item>
            <v-divider></v-divider>
            <v-list density="compact" nav>
                <div style="margin-left: 10px; margin-bottom: 20px; margin-top:10px;">
                    <label style="font-size: 14px">Tipo de grupo
                        <select name="tipoGrupo" id="select" v-model="selectTipo">
                            <option value="todos">Todos</option>
                            <option value="otro">Otro</option>
                            <option value="viaje">Viaje</option>
                            <option value="casa">Casa</option>
                            <option value="evento">Evento</option>
                            <option value="pareja">Pareja</option>
                            <option value="proyecto">Proyecto</option>
                        </select>
                    </label>
                </div>
                <Boton @click="aplicarFiltros" style="width: 80%; margin: 0px 25px 0 25px;" tipo="secundarioBorde">Aplicar
                    filtros</Boton>
            </v-list>
        </v-navigation-drawer>

        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2>Grupos en el sistema</h2>

            <div style="display:flex; align-items: center;width: 40%;">
                <input style="max-width: 500px; width: 100%;" class="textField" name="busqueda" v-model="busqueda" />
                <v-icon style="color:rgb(103, 103, 103); margin-left:10px ;" icon="mdi-magnify"></v-icon>
            </div>
            <div style="display:flex; align-items: center;">
                <div style="flex:1; display:flex; align-items: center;">

                    <p style="color:rgb(126, 126, 126)">Grupos por página</p>

                    <v-text-field density="compact" style="max-width: 80px;display:inline" :model-value="itemsPerPage"
                        class="pa-2" type="number" min="-1" max="15" hide-details
                        @update:model-value="itemsPerPage = parseInt($event, 10)"></v-text-field>

                </div>
                <v-icon @click.stop="drawer = !drawer" style="color:rgb(103, 103, 103);" icon="mdi-filter-outline"></v-icon>

            </div>


        </div>
        <div class="spinner" v-if="cargando">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
        <v-data-table :no-data-text="mensajeSinGrupos" v-else :items-per-page="itemsPerPage" :search="busqueda"
            v-model:page="page" :headers="headers" :items="gruposFiltrados" item-value="nombre" class="elevation-1">

            <template v-slot:item.imagen="{ value }">
                <img v-if="value == null || value == ''" src='/src/assets/svg/Group.svg' class="defaultPerfil" />
                <img v-else :src="`data:image/png;base64,${value}`" class="roundedd"/>
            </template>
            <template v-slot:item.menu="{ value, item }">
                
                    <div style="display:inline">
                        <v-icon id="actionIcon" style="padding-right: 4px;" size="small" icon="mdi-view-list" class="me-2"
                    @click="menuOpcion('Ver resúmen de actividad', item)" />
                    <v-tooltip activator="parent" location="bottom" >Resumen de actividad </v-tooltip>
                    </div>
            </template>
            <template v-slot:bottom>
                <div style="display:flex;">
                    <div style="flex:20;">
                        <div class="text-center pt-2">
                            <v-pagination style="margin-bottom: 10px;" v-model="page" :length="pageCount"></v-pagination>

                        </div>
                    </div>
                </div>
            </template>
        </v-data-table>
    </div>
</template>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
}

.imagenPerfil {
    display: inline;
    margin: 0 auto;
    margin-left: -25%;
    height: 100%;
    width: auto;
}

.defaultPerfil {
    width: 33px
}

.imagenWrapper {
    width: 35px;
    height: 35px;
    position: relative;
    overflow: hidden;
    border-radius: 50%;
    border-width: 3px;
    border-color: white;
    border-style: solid;
}

.textoVuetify {
    font-size: 14px;
}

#actionIcon:hover {
    color: var(--color-primario);
}

img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 35px;
    width: 35px;
    outline: 1px solid rgb(111, 111, 111);

}

</style>

<style scoped src="../../assets/css/formulario.css"/>