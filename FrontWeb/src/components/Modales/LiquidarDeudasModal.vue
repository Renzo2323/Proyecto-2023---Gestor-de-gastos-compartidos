<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import LiquidarDeudas from '../LiquidarDeudas.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue';

import axios from 'axios';

export default {
    name: 'nuevoIntegrante',
    components: {
        Boton,
        LiquidarDeudas,
        ContenedorCerrable
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
            conInvitacion: false
        }
    },
    emits: ['refetch'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.creado = false
            }

            this.dialog = valor
        }
    },
    props:{
        grupoId : Number,
        moneda : String
    }
}
</script>

<template>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
            <template v-if="!creado">
                <ContenedorCerrable style="width: 562px;" @cerrar="toggleDialog(false)">
                    <template v-slot:titulo>
                        Liquidar deudas
                    </template>
                    <template v-slot:contenido>
                        <LiquidarDeudas @refetch="$emit('refetch')" :grupoId="grupoId" :moneda="moneda" @cerrar-dialogo="toggleDialog(false)" @integrante-agregado="creado = true; conInvitacion = false; $emit('refetch')" @integrante-agregado-invitacion="creado = true; conInvitacion = true ;$emit('refetch')"/>
                    </template>
                </ContenedorCerrable>
            </template>
            <template v-else>
                <ContenedorCerrable style="padding-bottom: 34px;" @cerrar="toggleDialog(false)">
                    <template v-slot:titulo>
                        
                    </template>
                    <template v-slot:contenido>
                        <div>
                            <p>Se hizo algo</p>
                        </div>

                        <Boton style="display: block; margin-top: 25px;" tipo="secundarioBorde" @click="toggleDialog(false)">Cerrar</Boton>
                    </template>
                </ContenedorCerrable>
            </template>


    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>

