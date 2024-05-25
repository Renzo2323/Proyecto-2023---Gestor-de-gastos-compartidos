<script>
import Boton from '../Botones/Boton.vue';
export default {
    name: 'leftBarBase',
    components: {
        Boton
    },
    data() {
        return {
            opcionesBasicas: [
                { texto: 'Ajustes', icono: 'mdi-folder', ruta: '/IniciarSesion' },
                { texto: 'Ajustes2', icono: 'mdi-cash', ruta: '/CerrarSesion' },
            ],
            dialog: false,
            color: '#FF3D60'
        }
    },
    methods: {
        cambiarColor(color) {
            const r = document.querySelector(':root');
            r.style.setProperty('--color-primario', color);
        },
        volverColorOriginal(){
            const r = document.querySelector(':root');
            r.style.setProperty('--color-primario', '#FF3D60');
            this.color = '#FF3D60'
        }
    }
}
</script>

<template>
    <!-- <v-row justify="center">
        <v-dialog v-model="dialog" scrollable width="auto">

            <div
                style="background-color: white; padding: 15px; display: flex; flex-direction: column; align-items: center;">
                <v-color-picker mode="hex" hide-inputs elevation="0" v-model="color" style="margin-bottom: 5px;" />
                <Boton tipo="primario2" @click="cambiarColor(color)">Cambiar color</Boton>
                <v-btn class="text-none" variant="plain" @click="volverColorOriginal">Volver al color original</v-btn>
            </div>

        </v-dialog>
    </v-row> -->
    <v-navigation-drawer rail expand-on-hover permanent class="leftBar">
        <v-list>
            <slot>Contenido</slot>

        </v-list>
        <!-- <div style="display: flex; flex-direction: column; margin-left: 16px;">
            <v-icon id="iconColor" @click="dialog = true" icon="mdi-palette"
                style="justify-self: end; position: absolute; bottom: 15px"></v-icon>
        </div> -->
    </v-navigation-drawer>
</template>

<style scoped>
.leftBar {
    background-color: var(--color-primario);
    color: white;
}

.textoVuetify {
    font-size: 14px;
}

#iconColor{
    color: rgba(255, 255, 255, 0.535);
}
#iconColor:hover{
    color: white;
}
</style>