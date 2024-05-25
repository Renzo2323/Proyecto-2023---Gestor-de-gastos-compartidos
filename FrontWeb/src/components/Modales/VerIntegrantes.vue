<script>
import Boton from '../Botones/Boton.vue'
import Contenedor from '../Contenedores/Contenedor.vue'
import ContenedorCerrable from '../Contenedores/ContenedorCerrable.vue';
import InfoAvatar from '../../components/Modales/InfoAvatar.vue';

import axios from 'axios';

export default {
    name: 'verIntegrantes',
    components: {
        Boton,
        Contenedor,
        ContenedorCerrable,
        InfoAvatar
    },
    data() {
        return {
            dialog: false,
            errorPostSubmit: "",
            creado: false,
        }
    },
    emits: ['seleccionado', 'refetch'],
    methods: {
        toggleDialog(valor) {
            if (valor) {
                this.dialog = true
            } else {
                this.dialog = false
            }
        },
        cerrar() {
            this.toggleDialog(false)
        },
        infoIntegrante(integrante){
            
            this.$refs.infoAvatar.setAvatar(integrante)
            this.$refs.infoAvatar.toggleDialog(true)
        }
    },
    props: {
        grupo: String,
        integrantes: Array
    }
}
</script>

<template>
    <InfoAvatar ref="infoAvatar" :grupoId="$route.params.id" @refetch="$emit('refetch')"/>
    <v-dialog v-model="dialog" width="auto" persistent scrollable>
        <ContenedorCerrable style="width: 890px;" @cerrar="toggleDialog(false)">
            <template v-slot:titulo>
                <h5 style="text-align: center;">
                    Integrantes de <span style="color: var(--color-primario);">{{ grupo }}</span>

                </h5>
            </template>
            <template v-slot:contenido>
                <v-sheet class="overflow-y-auto" max-height="350" style="margin-top: 14px">
                    <div style="display:flex; flex-direction: column; gap: 4px;">


                        <template v-for="(integrante, i) in integrantes" :key="i">
                            <div @click="infoIntegrante(integrante)" id="itemIntegrante" style="display: flex; align-items: center; padding: 5px; border-radius: 8px;">
                                <!-- Sin imagen -->
                                <img v-if="integrante.imagen == null || integrante.imagen ==''" src="/src/assets/svg/User2.svg"
                                    style="width: 40px;margin-top: 0px" />
                                <!-- Con imagen -->
                                <img v-else :src="`data:image/png;base64,${integrante.imagen}`" class="roundedd" style="">
                                <div style="margin-left: 8px;">
                                    {{ integrante.nombre }}
                                </div>
                            </div>
                        </template>
                    </div>
                </v-sheet>
            </template>
        </ContenedorCerrable>
    </v-dialog>
</template>

<style scoped src="../../assets/css/formulario.css"/>

<style scoped>
img.roundedd {
    object-fit: cover;
    border-radius: 50%;
    height: 40px;
    width: 40px;
    outline: 1px solid rgb(111, 111, 111);

}

#itemIntegrante{

    outline: 1px solid rgb(195, 195, 195);
    margin: 1px;
    margin-right: 4px;
    cursor: pointer;
}

#itemIntegrante:hover{

background-color:rgb(237, 237, 237) ;
}
</style>