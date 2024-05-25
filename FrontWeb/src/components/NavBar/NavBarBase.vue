<script>

import Logo2 from '../icons/Logo2.vue';
import Boton from '../Botones/Boton.vue';
export default {
    name: 'navBarBase',
    components: {
        Logo2,
        Boton
    },
    data() {
        return {
            dialog: false,
            color: '#FF3D60'
        }
    },
    methods: {
        clickLogo() {
            //Si tiene una sesion abierta, deberia ir a otra pagina
            this.$router.push('/')
        },
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
    <v-dialog v-model="dialog" scrollable width="auto">

        <div style="background-color: white; padding: 15px; display: flex; flex-direction: column; align-items: center;">
            <v-color-picker mode="hex" hide-inputs elevation="0" v-model="color" style="margin-bottom: 5px;" />
            <Boton tipo="primario2" @click="cambiarColor(color)">Cambiar color</Boton>
            <v-btn class="text-none" variant="plain" @click="volverColorOriginal">Volver al color original</v-btn>
        </div>

    </v-dialog>
    <v-app-bar class="appBar" density="compact">
        <Logo2 @click="clickLogo" class="navBarLogo" />
        <div id="aa" @click="dialog = true" style="width: 3px; height: 3px; position: absolute; top: 0; cursor: pointer;"></div>

        <template v-slot:append>
            <div id="navBarRightElement">
                <li class="elementoNavBar">
                    <slot name="primero"></slot>
                </li>
                <li class="elementoNavBar">
                    <slot name="segundo"></slot>
                </li>
            </div>
        </template>


    </v-app-bar>
</template>


<style scoped>
ul {
    list-style-type: none;
    margin: 0;
    padding: 0;
    height: 52px;

    background-color: var(--color-primario) !;
    overflow: hidden;

}

.appBar {
    background-color: var(--color-primario) !important;
    list-style-type: none;
}

li {
    float: left;
    margin: 0;
}

#navBar {
    position: fixed;
    top: 0;
    width: 100%;
}

.floatLeft {
    float: left;
    height: 100%;
}

.floatRight {
    float: right;
    height: 100%;
    display: flex;
    align-items: center;
    margin-right: 10px;
}

#navBarRightElement {
    height: 100%;
    display: flex;
    align-items: center;
    margin-right: 10px;
}

.navBarLogo {
    margin: 9px;
    margin-left: 10px;
    margin-top: 11px;
}

.navBarLogo:hover {
    cursor: pointer;
}

#aa:hover {
    background-color: white;
}
</style>