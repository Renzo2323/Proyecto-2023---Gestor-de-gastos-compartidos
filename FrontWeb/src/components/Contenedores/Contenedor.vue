<template>
    <div class="contenedor">
        <h1>
            <slot name="titulo">
                <!-- Titulo -->
            </slot>
        </h1>

        <slot name="contenido">
            Contenitdo
        </slot>
    </div>
</template>

<style scoped>
.contenedor{

    border-radius: 6px;
    border-color: #8A8A8A;
    border-width: 1px;
    max-width: 562px;
    min-width: 400px;
    margin: auto;
    margin-top: 30px;
    box-shadow: 0px 0px 4px 2px rgba(0, 0, 0, 0.25);
    padding: 45px;
    background-color: white;
    overflow-y:auto;
    margin-bottom:30px;
}

h1{
    margin-top: 0;
    font-weight: 300;
}

</style>