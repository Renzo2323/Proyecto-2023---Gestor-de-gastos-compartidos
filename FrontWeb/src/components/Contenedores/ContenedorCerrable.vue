<script>

export default {
    name: 'contenedorCerrable',
    components: {
    },
    data() {
        return {
        }
    },
    emits: ['cerrar']
}
</script>

<template>
    <div class="contenedor">
        <div style="display:flex; justify-content: space-between;">
            <div></div>
            <div @click="$emit('cerrar')" style="cursor: pointer;">
                <v-icon>mdi-window-close</v-icon>
            </div>
        </div>
        <div style="padding-right: 35px; padding-top: 5px;">
            <h1>
                <slot name="titulo">
                    <!-- Titulo -->
                </slot>
            </h1>

            <slot name="contenido">
                Contenitdo
            </slot>
        </div>

    </div>
</template>

<style scoped>
.contenedor {

    border-radius: 6px;
    border-color: #8A8A8A;
    border-width: 1px;
    max-width: 562px;
    min-width: 400px;
    margin: auto;
    margin-top: 30px;
    box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
    padding: 45px;
    padding-top: 5px;
    padding-right: 10px;
    background-color: white;
    overflow-y: auto
}

h1 {
    margin-top: 0;
    font-weight: 300;
}
</style>