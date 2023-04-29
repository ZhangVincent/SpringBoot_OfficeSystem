<template>
  <div>
    <div>账号切换</div>
    <button @click="admin()" type="default" size="mini">admin</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <!-- <button @click="zhangsan()" type="default" size="mini">张三</button> -->
    <button @click="wjl()" type="default" size="mini">王经理</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQAKgKi4QgAAAA.0rJIdTc-BqNo1DYvuGhH_RkEhhTOXQE8g5pzW1EB2lhxhf4epyEStFLHFaXqyx2bICmyRCAeQJxlbu0D0MkKNA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    // zhangsan() {
    //   window.localStorage.setItem('token', '');
    //   let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3tzQxNLMw1VEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSjZVqAZJgwolDAAAA.eAGrymiYi_6Sb4KK3huGbxWXbu6DdLDQT1sGZUDaLa-OzUZTQhlEG9yS3LOjJEztmIJxAJvH5StubFlyzyqqhg'
    //   window.localStorage.setItem('token', token);
    //   this.token = window.localStorage.getItem('token')
    // },

    wjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kxRsjKCMPMSc1OBWsqzcpRqAY9PE6BAAAAA.fZPxJtIYhuerVYxWydx-Tw6adO45EpDA9FutGFfA1krTPgFjePcyy-OtPBYH_qGwaAmixZExX6MhX7qDWwHEmQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLMwMjUzNrYw0VEqLU4t8kxRsoIy8xJzU4FacjKLM5VqAdweFWJBAAAA.osPXengmcMnSq9x8csTG5ISFP3lTak5xZyVlAXQLO7q9hva8x2TeuLT2dpIr3jT0vdjepJqLCCsGEKVxrS1SgQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
