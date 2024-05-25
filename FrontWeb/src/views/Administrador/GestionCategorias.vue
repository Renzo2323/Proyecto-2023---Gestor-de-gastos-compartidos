<script>
import Boton from '../../components/Botones/Boton.vue';
import AgregarCategoriaModal from '../../components/Modales/AgregarCategoriaModal.vue'
import EditarCategoriaModal from '../../components/Modales/EditarCategoriaModal.vue'
import DeshabilitarCategoria from '../../components/Dialogos/DeshabilitarCategoria.vue';

import axios from 'axios';



export default {
    name: 'gestionCategorias',
    components: {
        Boton,
        AgregarCategoriaModal,
        DeshabilitarCategoria,
        EditarCategoriaModal
    },
    data() {
        return {
            itemsPerPage: 5,
            page: 1,
            headers: [
                { title: '', align: 'end', key: 'icono', sortable: false, width: '5%' },
                {
                    title: 'Nombre',
                    align: 'start',
                    key: 'nombre',
                },
                { title: 'Id', align: 'end', key: 'id', align: 'left' },
                { title: 'Descripcion', align: 'end', key: 'descripcion', align: 'left' },
                { title: '', align: 'end', key: 'menu', sortable: false },

            ],
            categorias: [],
            categoriasFiltradas: [],
            drawer: null,
            // selectTipo: "todos",
            cargando: false,

            busqueda: '',
            falloFetch: false,
            radioEstado: "ambas",
        }
    },
    methods: {
        aplicarFiltros() {
            let resultado = this.categorias
            if (this.radioEstado != "ambas") {
                if(this.radioEstado == "deshabilitada") resultado = resultado.filter((categoria) => !categoria.estado)
                else resultado = resultado.filter((categoria) => categoria.estado)
                
            }


            this.categoriasFiltradas = resultado

        },
        menuOpcion(opcion, item) {
            
            
            
            
            if (opcion == 'Deshabilitar') {
                this.$refs.deshabilitarDialogo.setearCategoria(item)
                this.$refs.deshabilitarDialogo.toggleDialog(true)
            } else if (opcion == 'Editar') {
                this.$refs.editarCategoriaModal.setearCategoria(item)
                this.$refs.editarCategoriaModal.toggleDialog(true)
            }
        },
        async fetchCategorias() {
            this.cargando = true
            this.categorias = []
            this.categoriasFiltradas = []
            const response = await axios.get(import.meta.env.VITE_API + "/api/categorias/usr", 
                        {headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                        }})
                .then(function (response) {
                    response.data.forEach(categoria => {
                        let descripcionAcortada = categoria.descripcion.slice(0, 30)
                        if (categoria.descripcion.length > 30) {
                            descripcionAcortada = descripcionAcortada + "..."
                        }
                        this.categorias.push({
                            id: categoria.id,
                            nombre: categoria.nombre,
                            descripcion: descripcionAcortada,
                            icono: categoria.icono,
                            estado: categoria.estado,
                            descripcionlarga: categoria.descripcion
                        })
                    });
                }.bind(this))
                .catch(function (error) {
                    // //debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
            this.aplicarFiltros()
            this.cargando = false
        },
        agregarCategoria() {
            
            this.$refs.agregarCategoriaModal.toggleDialog(true)

        },
        estiloBloqueado(item) {
            if (item.estado) {
                return 'color:var(--color-primario)'
            }
            return ''

        },
        estiloIconoBloqueado(item) {
            if (item.estado) {
                return 'mdi-toggle-switch'
            }
            return "mdi-toggle-switch-off-outline"
        }
    },
    async mounted() {
        this.$emit("terminoCargar")
        this.fetchCategorias()
    },
    computed: {
        pageCount() {
            return Math.ceil(this.categoriasFiltradas.length / this.itemsPerPage)
        },
        mensajeSinCategorias() {
            if (this.falloFetch) {
                return 'No se pudieron traer las categorias de la base de datos'
            }

            if (this.categorias.length > 0) {
                return 'Ninguna categoria coincide con los parametros'
            } else {
                return 'No hay categorias en el sistema'
            }

        }
    },
}
</script>



<template>
    <AgregarCategoriaModal ref="agregarCategoriaModal" @refetch="fetchCategorias" />
    <EditarCategoriaModal ref="editarCategoriaModal" @refetch="fetchCategorias" />
    <DeshabilitarCategoria ref="deshabilitarDialogo" @refetch="fetchCategorias" />

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
                <!-- Aqui van filtros -->
                <v-list-item style="margin-bottom: 0; padding-bottom: 0;">
                    <v-list-item-title class="textoVuetify">Estado</v-list-item-title>
                </v-list-item>
                <v-radio-group v-model="radioEstado">
                    <v-radio value="deshabilitada" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Deshabilitada</div>
                        </template>
                    </v-radio>
                    <v-radio value="habilitada" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Habilitada</div>
                        </template>
                    </v-radio><v-radio value="ambas" class="textoVuetify">
                        <template v-slot:label>
                            <div class="textoVuetify">Ambas</div>
                        </template>
                    </v-radio>
                </v-radio-group>
                <Boton @click="aplicarFiltros" style="width: 80%; margin: 0px 25px 0 25px;" tipo="secundarioBorde">Aplicar
                    filtros</Boton>
            </v-list>
        </v-navigation-drawer>

        <div style="display: flex; justify-content: space-between; align-items: center;">
            <h2>Categorias en el sistema</h2>

            <div style="display:flex; align-items: center;width: 40%;">
                <input style="max-width: 500px; width: 100%;" class="textField" name="busqueda" v-model="busqueda" />
                <v-icon style="color:rgb(103, 103, 103); margin-left:10px ;" icon="mdi-magnify"></v-icon>
            </div>
            <div style="display:flex; align-items: center;">
                <div style="flex:1; display:flex; align-items: center;">

                    <p style="color:rgb(126, 126, 126)">Categorias por página</p>

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
        <v-data-table :no-data-text="mensajeSinCategorias" v-else :items-per-page="itemsPerPage" :search="busqueda"
            v-model:page="page" :headers="headers" :items="categoriasFiltradas" item-value="nombre" class="elevation-1">

            <template v-slot:item.icono="{ value }">
                <v-icon style="font-size: 24px; padding: 16px; margin: 0; color: rgb(69, 69, 69);"> mdi-{{ value }}
                </v-icon>
            </template>
            <template v-slot:item.menu="{ value, item }">

                <div style="display:inline">
                    <v-icon :style="estiloBloqueado(item)" id="actionIcon" style="padding-right: 4px; font-size: 30px;"
                    size="small" :icon="estiloIconoBloqueado(item)" class="me-2"
                    @click="menuOpcion('Deshabilitar', item)" />
                    <v-tooltip activator="parent" location="bottom" >{{item.estado ? 'Deshabilitar': 'Habilitar'}} </v-tooltip>
                </div>


                <div style="display:inline">
                    <v-icon id="actionIcon" style="padding-right: 4px;" size="small" icon="mdi-pencil" class="me-2"
                    @click="menuOpcion('Editar', item)" />
                    <v-tooltip activator="parent" location="bottom" >Editar </v-tooltip>
                </div>
                
                
                
            </template>


            <template v-slot:bottom>
                <div style="display:flex;">
                    <div style="flex:20;">
                        <div class="text-center pt-2">
                            <v-pagination style="margin-bottom: 10px;" v-model="page" :length="pageCount"></v-pagination>

                        </div>
                    </div>
                    <div style="flex:1; display:flex; align-items: center;">
                        <div
                            style="background-color: var(--color-primario); padding: 0px; border-radius: 50%; width: 36px; height: 36px;">
                            <v-icon style="width: 100%;height: 100%" color="white" id="actionIcon" icon="mdi-plus"
                                @click="agregarCategoria" />
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
</style>

<style scoped src="../../assets/css/formulario.css"/>