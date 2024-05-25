<template>
    <div>
      
    </div>
  </template>
  
  <script>
  export default {
    name: 'WellKnown',
    props: {
      file: String
    },
    mounted () {
      window.open(this.file)
    }
  }
  </script>