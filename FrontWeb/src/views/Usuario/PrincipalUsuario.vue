<script>
import axios from 'axios';
import Boton from '../../components/Botones/Boton.vue';

export default {
    name: 'principalUsuario',
    data() {
        return {
            grupos: []
        }
    },
    components: {
        Boton
    },
    methods: {
        async fetchGrupos() {
            const url = import.meta.env.VITE_API + "/api/grupos/usf/obtenerMisGrupos"
            this.grupos = []
            const response = await axios.get(url,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`,
                    }
                })
                .then(function (response) {
                    this.grupos = response.data

                }.bind(this))
                .catch(function (error) {
                    this.errorPostSubmit = "No se pudo realizar esta accion"
                }.bind(this));

            this.$emit("terminoCargar")
        },
        tipoGrupoCapitalizado(tipoGrupo) {
            return tipoGrupo.charAt(0).toUpperCase() + tipoGrupo.slice(1).toLowerCase()
        },
        getIconoTipoGrupo(tipoGrupo) {
            let tipo = tipoGrupo
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
        irAGrupo(id) {
            const ruta = "/grupo/" + id

            this.$router.push(ruta)
        }
    },
    mounted() {
        this.fetchGrupos()

    },
    computed: {

    },
}
</script>

<template>
    <div id="contenedor1">
        <div v-if="grupos.length == 0" style="text-align: center; margin-top: 160px; display: flex; flex-direction: column;">
            No tiene ningún grupo
            <Boton @click="$router.push('/crearGrupo')" style="align-self:center; margin-top: 15px;" tipo="primario2">Crear un nuevo grupo</Boton>
        </div>
        <div v-else>
            <h2>Mis grupos</h2>
            <div id="grid" style="display: flex; flex-wrap: wrap; gap: 20px; margin-top: 20px; align-content: flex-start;">
                <template v-for="(grupo, i) in grupos">
                    <v-card @click="irAGrupo(grupo.id)"
                        style="margin-bottom: 10px; border-width: 1px; border-style:solid; border-color:rgb(202, 202, 202); width: 422px;">
                        <div class="d-flex flex-no-wrap ">
                            <!-- Sin imagen -->
                            <img v-if="grupo.imagen == null || grupo.imagen ==''" src='/src/assets/images/Grupo.png' style="width: 165px;"
                                class="squared" />
                            <!-- Con imagen -->
                            <img v-else :src="`data:image/png;base64,${grupo.imagen}`" class="squared">
                            <div style="margin-top: 15px; margin-right: 15px;">
                                <v-chip size="small"
                                    style="cursor: pointer;font-size: 11px; background-color: var(--color-primario); color: white; margin-left: 10px;">
                                    <template v-slot:prepend>
                                        <v-icon style="margin-right: 5px;">{{ getIconoTipoGrupo(grupo.tipoGrupo) }}</v-icon>
                                    </template>
                                    {{ tipoGrupoCapitalizado(grupo.tipoGrupo) }}
                                </v-chip>
                                <v-card-title style="font-size: 16px;">
                                    {{ grupo.nombre }}
                                </v-card-title>

                                <div v-if="grupo.saldoLocal < 0" style="display: flex;">
                                    <p style="margin-left: 12px; margin-top: 3px;">Debes</p>
                                    <!-- Luego hacer que muestre la moneda correcta con this.moneda y la funcion que retorna el signo -->
                                    <v-chip
                                        style="cursor: pointer;font-size: 16px; margin-left: 8px; margin-right:10px; background-color: var(--color-primario); color: white;">
                                        ${{ Math.abs(grupo.saldoLocal) }}
                                    </v-chip>
                                </div>
                                <div v-if="grupo.saldoLocal > 0" style="display: flex;">
                                    <p style="margin-left: 12px; margin-top: 3px;">Te deben</p>
                                    <!-- Luego hacer que muestre la moneda correcta con this.moneda y la funcion que retorna el signo -->
                                    <v-chip
                                        style="cursor: pointer;font-size: 16px; margin-left: 8px; margin-right:10px; background-color: var(--color-primario); color: white;">
                                        ${{ grupo.saldoLocal }}
                                    </v-chip>
                                </div>
                                <div v-if="grupo.saldoLocal == 0" style="display: flex;">
                                    <p style="cursor: pointer;margin-left: 12px; margin-top: 3px;">No tienes deudas</p>
                                </div>


                            </div>


                        </div>
                    </v-card>
                </template>
            </div>
        </div>

    </div>
</template>

<style scoped>
#contenedor1 {
    margin: auto;
    max-width: 80%;
    margin-top: 30px;
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

/* #grid::after{
    content: '';
    flex: auto;
    gap: 20px;
} */
</style>