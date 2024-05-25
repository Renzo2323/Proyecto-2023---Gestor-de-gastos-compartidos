<script>
import Boton from '../../components/Botones/Boton.vue';

import axios from 'axios';

export default {
    name: 'gestionUsuarios',
    components: {
        Boton,
    },
    data() {
        return {
            parametros: [],
            parametrosOriginales: [],
            headers: [
                {
                    title: 'Nombre',
                    align: 'start',
                    key: 'nombre',
                    sortable: false
                },
                { title: 'Valor', align: 'left', key: 'valor', sortable: false },
                { title: '', align: 'end', key: 'menu', sortable: false },


            ],
            aaa: 0,
            cargando: false
        }
    },
    methods: {
        async fetchParametros() {
            this.cargando = true
            const url = import.meta.env.VITE_API + "/api/parametros/adm/"
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
                        this.parametros = []
                        this.parametrosOriginales = []
                        response.data.forEach(parametro => {
                            this.parametros.push({
                                id: parametro.id,
                                nombre: parametro.nombre,
                                valor: parametro.valor,
                            })

                            this.parametrosOriginales.push({
                                id: parametro.id,
                                nombre: parametro.nombre,
                                valor: parametro.valor,
                            })
                        });


                    }

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

                this.cargando = false
        },
        nuevoParametro() {
            this.parametros.push({
                id: null,
                nombre: 'NUEVO_PARAMETRO',
                valor: 'valorParametro'
            })
        },
        reestablecerParametros() {
            this.parametros = []
            this.parametrosOriginales.map(p => {
                this.parametros.push(p)
            })
            this.aaa += 1
        },
        eliminar(item) {
            
            let index = this.parametros.indexOf(item)
            if (index !== -1) {
                this.parametros.splice(index, 1)
            }
        },
        async guardarCambios() {
            const url = import.meta.env.VITE_API + "/api/parametros/adm/reemplazarParametros/"
            const response = await axios.post(url, this.parametros,
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
                        this.fetchParametros()


                    }

                }.bind(this))
                .catch(function (error) {
                    // debugger
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));
        }
    },
    async mounted() {
        this.fetchParametros()
        this.$emit("terminoCargar")

    },
    computed: {
    },
}
</script>



<template>
    <div id="contenedor1">

        <h2>Parametros</h2>
        <!-- <h3>Parametros normales</h3>
        {{ parametros }}
        <br>
        <br>
        <h3>Parametros originales</h3>
        {{ parametrosOriginales }} -->
        <div class="spinner" v-if="cargando">
            <div class="rect1"></div>
            <div class="rect2"></div>
            <div class="rect3"></div>
            <div class="rect4"></div>
            <div class="rect5"></div>
        </div>
        <v-data-table v-else no-data-text="No hay parametros" items-per-page="1000" :headers="headers" :items="parametros"
            class="elevation-1">

            <template v-slot:item.nombre="{ value, item }">
                <v-hover v-slot="{ isHovering, props }">
                    <v-text-field v-bind="props" :key="aaa" hide-details density="compact"
                        :variant="isHovering ? 'underlined' : 'plain'" :model-value="value" v-model="item.nombre" />
                    <!-- Conectar de una manera esto al array de parametros -->
                </v-hover>
            </template>



            <template v-slot:item.valor="{ value, item }">
                <v-hover v-slot="{ isHovering, props }">
                    <v-text-field v-bind="props" :key="aaa" hide-details density="compact"
                        :variant="isHovering ? 'underlined' : 'plain'" :model-value="value" v-model="item.valor" />
                </v-hover>
            </template>

            <template v-slot:item.menu="{ value, item }">

                <div style="display:inline">
                    <v-icon id="actionIcon" style="padding-right: 4px" size="small" icon="mdi-window-close" class="me-2"
                        @click="eliminar(item)" />
                    <v-tooltip activator="parent" location="bottom">Eliminar
                    </v-tooltip>
                </div>
            </template>


            <template v-slot:bottom>
                <div style="display:flex;">
                    <div style="flex:30;">
                        <Boton @click="guardarCambios" style="margin-left: 15px; margin-top: 8px; margin-bottom: 15px;"
                            tipo="primario2">Guardar
                            cambios</Boton>
                        <Boton @click="reestablecerParametros"
                            style="margin-left: 15px; margin-top: 8px; margin-bottom: 15px;" tipo="secundarioBorde">
                            Reestablecer parametros</Boton>
                    </div>
                    <div style="flex:1; display:flex; align-items: center;">
                        <div
                            style="background-color: var(--color-primario); padding: 0px; border-radius: 50%; width: 36px; height: 36px; margin-bottom: 8px; margin-right: 8px;">
                            <v-icon style="width: 100%;height: 100%; cursor: pointer;" color="white" id="actionIcon"
                                icon="mdi-plus" @click="nuevoParametro" />
                                <v-tooltip activator="parent" location="bottom">Nuevo parametro
                    </v-tooltip>
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

#contenedorParametro {
    display: flex;
    gap: 15px;
}

#contenedorListaParametros {
    border: 1px solid #8A8A8A;
    border-radius: 10px;
    padding: 15px;

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