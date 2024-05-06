<template>
  <div class="register-container">
    <el-form ref="registerForm" class="register-form" label-position="left"
             :model="registerForm" :rules="registerRules">
      <div class="title-container">
        <h3 class="title">{{ $t('register.title') }}</h3>
        <lang-select class="set-language"/>
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
          <span class="show-pwd" @click="showPassword('password')">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </span>
        </el-form-item>
      </el-tooltip>
      <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
        <el-form-item prop="confirmPassword">
          <span class="svg-container"><svg-icon icon-class="password"/></span>
          <el-input ref="confirmPassword" :key="passwordType"
                    v-model="registerForm.confirmPassword" :type="passwordType"
                    name="confirmPassword" tabindex="3" auto-complete="on"
                    :placeholder="$t('register.confirmPassword')"
                    @keyup.native="checkCapslock"
                    @keyup.enter.native="handleRegister"
                    @blur="capsTooltip = false"/>
          <span class="show-pwd" @click="showPassword('confirmPassword')">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"/>
          </span>
        </el-form-item>
      </el-tooltip>
      <el-row>
        <el-col :span="12">
          <el-form-item prop="kaptcha">
            <span class="svg-container"><svg-icon icon-class="user"/></span>
            <el-input ref="kaptcha" v-model="registerForm.kaptcha" name="kaptcha" type="text"
                      tabindex="4" :placeholder="$t('register.kaptcha')"/>
          </el-form-item>
        </el-col>
        <el-col :span="11" :push="1">
          <img class="pointer" :src="kaptchaUrl" alt="Kaptcha" @click="refreshKaptchaUrl">
        </el-col>
      </el-row>
      <el-button type="primary" style="width:100%;margin-bottom:32px;"
                 :loading="loading" @click.native.prevent="handleRegister">
        {{ $t('register.submit') }}
      </el-button>
    </el-form>
  </div>
</template>

<script>

import {register} from '@/api/account';
import LangSelect from '@/components/LangSelect.vue';
import {validPassword, validRegisterUsername} from '@/utils/validate';

export default {
  name: 'Register',
  components: {LangSelect},
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
      },
      registerRules: {
        username: [{required: true, trigger: 'change', validator: this.validateUsername}],
        password: [{required: true, trigger: 'change', validator: this.validatePassword}],
        confirmPassword: [{required: true, trigger: 'change', validator: this.validateConfirmPassword}],
      },
      capsTooltip: false,
      passwordType: 'password',
      loading: false,
      kaptchaUrl: process.env.VUE_APP_BASE_API + '/kaptcha?d=' + new Date() * 1,
    };
  },
  mounted() {
    if (this.registerForm.username === '') {
      this.$refs.username.focus();
    } else if (this.registerForm.password === '') {
      this.$refs.password.focus();
    } else if (this.registerForm.confirmPassword === '') {
      this.$refs.confirmPassword.focus();
    }
  },
  methods: {
    validateUsername(rule, value, callback) {
      if (!validRegisterUsername(value)) {
        callback(new Error('Please enter the correct username'));
      } else {
        callback();
      }
    },
    validatePassword(rule, value, callback) {
      if (!validPassword(value)) {
        callback(new Error('The password can not be less than 6 digits'));
      } else {
        this.$refs.registerForm.validateField('confirmPassword');
        callback();
      }
    },
    validateConfirmPassword(rule, value, callback) {
      if (!value) {
        return;
      }
      if (!validPassword(value)) {
        callback(new Error('The confirm password can not be less than 6 digits'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('The confirm password must be the same as the password'));
      } else {
        callback();
      }
    },
    checkCapslock(e) {
      const {key} = e;
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z');
    },
    refreshKaptchaUrl() {
      this.kaptchaUrl = process.env.VUE_APP_BASE_API + '/kaptcha?d=' + new Date() * 1;
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.registerForm).then(() => {
            // TODO i18n
            this.$message.success(`账户 [${this.registerForm.username}] 注册成功！`);
            this.$router.push('/login');
          }).catch(() => {
            this.loading = false;
          });
        } else {
          console.log('register submit error!!');
          return false;
        }
      });
    },
    showPassword(element) {
      if (this.passwordType === 'password') {
        this.passwordType = '';
      } else {
        this.passwordType = 'password';
      }
      this.$nextTick(() => {
        if (element === 'password') {
          this.$refs.password.focus();
        } else if (element === 'confirmPassword') {
          this.$refs.confirmPassword.focus();
        }
      });
    },
  }
};
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
    padding: 160px 24px 0;
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
