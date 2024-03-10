<template>
  <div class="register-container">
    <el-form ref="registerForm" class="register-form" label-position="left"
             :model="registerForm" :rules="registerRules">
      <div class="title-container">
        <h3 class="title">注册账户</h3>
      </div>
      <el-form-item prop="username">
        <span class="svg-container"><svg-icon icon-class="user"/></span>
        <el-input ref="username" v-model="registerForm.username" name="username" type="text"
                  tabindex="1" auto-complete="on" :placeholder="$t('register.username')"/>
      </el-form-item>
      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="password">
          <span class="svg-container"><svg-icon icon-class="password"/></span>
          <el-input ref="password" :key="passwordType"
                    v-model="registerForm.password" :type="passwordType"
                    name="password" tabindex="2" auto-complete="on"
                    :placeholder="$t('register.password')"
                    @keyup.native="checkCapslock"
                    @keyup.enter.native="handleRegister"
                    @blur="capsTooltip = false"/>
          <span class="show-pwd" @click="showPassword">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </span>
        </el-form-item>
      </el-tooltip>
      <el-form-item prop="confirmPassword">
        <span class="svg-container"><svg-icon icon-class="password"/></span>
        <el-input ref="confirmPassword" :key="passwordType"
                  v-model="registerForm.confirmPassword" :type="passwordType"
                  name="confirmPassword" tabindex="3" auto-complete="on"
                  :placeholder="$t('register.confirmPassword')"/>
      </el-form-item>
      <el-form-item prop="nickname">
        <span class="svg-container"><svg-icon icon-class="user"/></span>
        <el-input ref="nickname" v-model="registerForm.nickname" name="nickname" type="text"
                  tabindex="4" :placeholder="$t('register.nickname')"/>
      </el-form-item>
      <el-form-item prop="avatar">
      </el-form-item>
      <el-form-item prop="gender">
      </el-form-item>
      <el-form-item prop="birthday">
      </el-form-item>
      <el-form-item prop="email">
        <span class="svg-container"><svg-icon icon-class="user"/></span>
        <el-input ref="email" v-model="registerForm.email" name="email" type="text"
                  tabindex="5" :placeholder="$t('register.email')"/>
      </el-form-item>
      <el-form-item prop="phoneNumber">
        <span class="svg-container"><svg-icon icon-class="user"/></span>
        <el-input ref="phoneNumber" v-model="registerForm.phoneNumber" name="phoneNumber" type="text"
                  tabindex="6" :placeholder="$t('register.phoneNumber')"/>
      </el-form-item>
      <el-form-item prop="introduction">
        <el-input ref="introduction" v-model="registerForm.introduction" name="introduction" type="textarea"
                  tabindex="7" :placeholder="$t('register.introduction')"/>
      </el-form-item>
      <el-button type="primary" style="width:100%;margin-bottom:32px;"
                 :loading="loading" @click.native.prevent="handleRegister">
        {{ $t('register.register') }}
      </el-button>
    </el-form>
  </div>
</template>

<script>

export default {
  name: 'Register',
  components: {},
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        nickname: '',
        email: '',
        avatar: '',
        gender: '',
        birthday: '',
        phoneNumber: '',
        introduction: '',
      },
      registerRules: {},
      capsTooltip: false,
      passwordType: 'password',
      loading: false,
    }
  },
  methods: {
    checkCapslock() {

    },
    handleRegister() {

    },
    showPassword() {

    },
  }
}
</script>

<style lang="scss">
$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .register-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.register-container {
  .el-input {
    display: inline-block;
    height: 48px;
    width: 80%;

    input {
      background: transparent;
      border: 0;
      -webkit-appearance: none;
      border-radius: 0;
      padding: 8px 0 8px 0;
      color: $light_gray;
      height: 48px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.register-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .register-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 48px 24px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 8px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 8px 16px 8px 16px;
    color: $dark_gray;
    vertical-align: middle;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0 auto 40px auto;
      text-align: center;
      font-weight: bold;
    }

    .set-language {
      color: #fff;
      position: absolute;
      top: 4px;
      font-size: 18px;
      right: 0;
      cursor: pointer;
    }
  }

  .show-pwd {
    position: absolute;
    right: 16px;
    top: 8px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
