<script>

export default {
  name: 'principal',
  components: {
  },
  mounted() {
    this.$emit("terminoCargar")
  }
}
</script>

<template>
  <div class="imgContainer">
    <img src="/src/assets/images/logo_animado.gif" style="width: 300px"/>
  </div>
<div style="display:flex; flex-direction: column; align-items: center; margin-bottom: 300px;">
  <div style="display:flex; align-items: center;">
    <img id="bordeImagen" src="/src/assets/images/Borde1B.png">
    <div class="container2">
    <div class="justificado">
      <p style="text-align: center;">
        ¡Bienvenido a <strong>SplitIt</strong>! Tu compañero definitivo para gestionar gastos compartidos de forma
        sencilla y eficiente!
        Imagina un mundo donde organizar tus gastos entre amigos sea tan fácil como apretar un botón. Con
        <strong>SplitIt</strong>, esa visión se convierte en realidad.
      </p>

    </div>
  </div>
  <img id="bordeImagen" src="/src/assets/images/Borde2B.png">
  </div>
  
  
  <div class="container">
    <div class="justificado">
      <p>
        La intuitiva interfaz de <strong>SplitIt</strong> te permite invitar a tus amigos y crear grupos personalizados
        para llevar un registro transparente de todos los gastos compartidos.
        Olvídate de las molestias de dividir cuentas después de una cena o un viaje. Con <strong>SplitIt</strong>, puedes
        agregar gastos en simples pasos, indicando quién pagó y quién participó en el gasto.
        Lo mejor de todo es que <strong>SplitIt</strong> hace el trabajo pesado por ti. Calcula automáticamente cuánto
        debe pagar cada persona, evitando cualquier confusión o malentendido.
      </p>
      <p>
        Pero eso no es todo, <strong>SplitIt</strong> va más allá al permitirte liquidar las deudas de manera sencilla y
        rápida. Nunca más tendrás que preocuparte por quién debe cuánto a quién.
        Con solo unos clics, podrás resolver las deudas pendientes y mantener tus relaciones sin complicaciones.
        Únete a nuestra comunidad de usuarios satisfechos y descubre cómo <strong>SplitIt</strong> puede hacer que la
        gestión de gastos compartidos sea una experiencia sin estrés.
        ¡Crea una cuenta o descarga nuestra aplicación móvil ahora y comienza a simplificar tu vida con <strong>SplitIt</strong>!
      </p>
    </div>
  </div>
</div>
</template>
<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  background-color: rgb(255, 255, 255);

}

.container2{
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #FF3D60;
  margin-top: 11px;
  width: 930px;
  /* border-radius: 10px; */

  .justificado p{
    color: white;
    
  }
  .justificado strong{
    color: white;
  }
  
}

#bordeImagen{
  height: 124px;
}

p {
  margin-bottom: 20px;
}

strong {
  color: #FF3D60;
}

.justificado {
  text-align: justify;
  color: #000000;
  width: 800px;
  font-weight: 900;
  font-family: 'Lexend';
  margin-top: 20px;
}

.imgContainer {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
  margin-bottom: 45px;
}</style>