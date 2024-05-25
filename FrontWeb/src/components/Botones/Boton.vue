<script>

export default{
    name: 'boton',
    data() {
        return {
        }
    },
    props: {
        tipo: String,
        icono: String,
        iconoCustom: String,
        loading: Boolean
    }
}
</script>

<template>
    <v-btn :loading="loading" :prepend-icon="icono" :class="tipo" class="text-none botonVuetify" variant="flat">
        <span class="icono" :class="iconoCustom"></span>
        <slot> Boton </slot>
    </v-btn>
    <!-- <button class="Boton" :class="tipo">
        <span class="icono" :class="icono"></span>
        <slot> Boton </slot>
    </button> -->
</template>

<style scoped>
/* No se usa */
.Boton { 
  background-color: #ffffff; 
  border: none;
  color: var(--color-primario);

  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 1.6rem;
  font-family: inherit;
  font-weight:500;
  padding-top: 1.28rem;
  padding-bottom: 1.28rem;
  padding-left: 2rem;
  padding-right: 2rem;
  border-radius: 9px;
  line-height: 110%;
  font-weight:400;
}

.Boton:hover{
    cursor: pointer;
    filter: brightness(95%);
}

.botonVuetify{
    font-size: 14px;
}

.primario{
    background-color: var(--color-primario);
    color: #ffffff;
}

.primario2{
    background-color: var(--color-primario);
    color: #ffffff;
}

.secundario{
    background-color: #ffffff;
}

.secundarioBorde{
    background-color: #ffffff;
    border: 1px solid #4B4B4B;
}

.chico{
    padding-top: 1rem;
    padding-bottom: 1rem;
}

.google{
    border: 1px solid #4B4B4B;
    color: #4B4B4B;
    padding-left: 12px;
}

.icono{
    display: none;
}

.iconoGoogle{
    display: block;
    background: url(../../assets/images/google-logo.png) no-repeat;
    float: left;
    width: 30px;
    height: 17px;
    background-position: center;
    background-size: contain;
    margin-right: 10px;

}

.iconoGasto{
    display: block;
    background: url(../../assets/svg/GastoIcono.svg) no-repeat;
    float: left;
    width: 30px;
    height: 17px;
    background-position: center;
    background-size: contain;
    margin-right: 10px;

}

.iconoLiquidarDeudas{
    display: block;
    background: url(../../assets/svg/LiquidarDeudas.svg) no-repeat;
    float: left;
    width: 30px;
    height: 17px;
    background-position: center;
    background-size: contain;
    margin-right: 10px;

}
</style>