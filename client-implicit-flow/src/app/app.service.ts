import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {OAuthService} from 'angular-oauth2-oidc';
import {Observable} from 'rxjs';

export class Foo {
  constructor(
    public id: number,
    public name: string) { }
}

@Injectable()
export class AppService {

  constructor(
    private _router: Router,
    private _http: HttpClient,
    private _oauthService: OAuthService
  ) {
    this._oauthService.configure({
      loginUrl: 'http://localhost:8081/spring-security-oauth-server/oauth/authorize',
      redirectUri: 'http://localhost:8086/',
      clientId: 'sampleClientId',
      scope: 'read write foo bar',
      oidc: false,
    });

    this._oauthService.setStorage(sessionStorage);
    this._oauthService.tryLogin({});
  }

  isLoggedIn() {
    console.log('access token: ' + this._oauthService.getAccessToken());
    return this._oauthService.getAccessToken() !== null;
  }

  getAccessToken() {
    this._oauthService.initImplicitFlow();
  }

  logout() {
    this._oauthService.logOut();
    location.reload();
  }

  getResource(resourceUrl): Observable<any> {
    const headers = new HttpHeaders({
      'Content-type': 'application/json;charset=UTF-8',
      Authorization: 'Bearer ' + this._oauthService.getAccessToken(),
    });

    return this._http.get(resourceUrl, { headers });
  }
}
