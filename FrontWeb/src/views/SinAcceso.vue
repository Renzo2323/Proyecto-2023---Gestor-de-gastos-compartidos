<script>
import Boton from '../components/Botones/Boton.vue'
export default{
    name: 'sinAcceso',
    components: {
        Boton
    },
    mounted(){
        this.$emit("terminoCargar")
    }
}
</script>

<template>
        <div class="imgContainer">
        <div class="imagen"></div>
    </div>
    <h2 style="text-align: center;">
        No tiene acceso 
    </h2>
    <div style="display: flex; justify-content: center; margin-top: 18px; margin-bottom: 200px;">
        
        <Boton @click="$router.push('/')" tipo="primario2">Volver a la pagina principal</Boton>
    </div>
</template>

<style scoped>
.imgContainer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
  margin-bottom: 25px;
}

.imagen {
  background-image: url(/src/assets/images/403.gif);
  width: 570px;
  height: 285px;
  box-shadow: 0 0 16px 20px white inset;
}

</style>