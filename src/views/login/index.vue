

<template>
  <div id="body">
    <div id="app" class="container">
      <img src="@/assets/images/login-background.jpg" />
      <div class="panel">
        <div class="content login" >
          <div class="switch">
            <span>登录</span>
          </div>
          <el-form class="form" id="fromLogin" ref="loginForm" :model="loginForm" :rules="loginRules" auto-complete="on" label-position="left" @keyup.enter.native="handleLogin">
              <div class="input">
                <input
                  :class="{ hasValue: loginForm.username !==''}"
                  v-model="loginForm.username"
                  type="text"
                  name="username"
                  id="username"
                /><label for="username">用户名</label>
              </div>
              <div class="input">
                <input 
                  :class="{ hasValue: loginForm.password !==''}"
                  v-model="loginForm.password"
                  type="password"
                  name="password"
                  id="password"
                /><label for="password">密码</label>
              </div>


            <el-button :loading="loading" type="primary" @click.native.prevent="handleLogin" >登录</el-button>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
    

<script>
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 4) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '222021321262020', // 表单自动填充为admin和111111
        password: '1234'
        // username:'',
        // password:''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    // showPwd() {
    //   if (this.passwordType === 'password') {
    //     this.passwordType = ''
    //   } else {
    //     this.passwordType = 'password'
    //   }
    //   this.$nextTick(() => {
    //     this.$refs.password.focus()
    //   })
    // },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true // 如果成功则跳转到首页
          this.$store.dispatch('Login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
};
</script>
<style scoped>
@font-face {
  font-family: miaowu;
  src: require("@/assets/font/login-font.TTF");
}
#body {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: miaowu;
  /* background: linear-gradient(45deg, rgb(181, 154, 254), rgb(245, 189, 253)) */
    /* fixed; */
}

* {
    margin: 0;
    padding: 0;
}
.container {
  position: relative;
  width: 70rem;
}

.container img {
  margin-top:70px;
  border-radius: 0.5%;
  width: 70rem;
	box-shadow: 15px 15px 15px 15px  lightgrey;
	/* border: 1px solid yellow; */
}

.switch span {
  color:#7c1823;
  font-size: 1.4rem;
  cursor: pointer;
}


.panel {
  width: 30%;
  margin: 10rem 0 0;
  position: absolute;
  right: 0;
  top: 0;

  display: flex;
  justify-content: center;
}

.form {
  width: 12rem;
  margin: 3rem 0 0;
}

.form .input {
  position: relative;
  opacity: 1;
  height: 2rem;
  width: 100%;
  margin: 2rem 0;
  transition: 0.4s;
}

.input input {
  outline: none;
  width: 100%;
  border: none;
  border-bottom: 0.1rem solid #7c1823;
  position: relative;
  line-height: 35px;
  background: transparent;
  z-index: 1;
}

.input label {
  position: absolute;
  left: 0;
  top: 20%;
  font-size: 1.2rem;
  color: #7c1823;
  transition: 0.3s;
}

/* fixbug for IMBIT（1448214956） */
.hasValue ~ label,
input:focus ~ label {
  top: -50%;
  font-size: 0.9rem;
}

.form span {
  display: block;
  color: #7c1823;
  font-size: 0.8rem;
  cursor: pointer;
}

.form button {
  border: none;
  outline: none;
  margin: 2.5rem 0 0;
  width: 100%;
  height: 3rem;
  border-radius: 3rem;
  /* background: linear-gradient(90deg, rgb(181, 154, 254), rgb(245, 189, 253));
	*/
 font-size:16px;
	background-color: #7c1823;
  /* box-shadow: 0 0 8px #7c1823; */
  cursor: pointer;
  color: white;
  font-family: miaowu;
}

#live2dcanvas {
  border: 0 !important;
}
</style>