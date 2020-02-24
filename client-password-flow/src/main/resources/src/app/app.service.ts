import {Router} from '@angular/router';
import {Cookie} from 'ng2-cookies';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

export class Foo {
  constructor(
    public id: number,
    public name: string) { }
}

@Injectable()
export class AppService {

  constructor(private _router: Router, private _http: HttpClient) {}

  obtainAccessToken(loginData) {

    const params = new URLSearchParams();

    params.append('username', loginData.username);
    params.append('password', loginData.password);
    params.append('grant_type', 'password');
    params.append('client_id', 'fooClientIdPassword');

    const headers = new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Basic ' + btoa('fooClientIdPassword:secret')
    });

    console.log(params.toString());

    this._http.post(
      'http://localhost:8081/spring-security-oauth-server/oauth/token',
      params.toString(),
      { headers }
      )
      .subscribe(
        data => this.saveToken(data),
        err => alert('Invalid Credentials')
      );
  }

  saveToken(token) {
    const expireDate = new Date().getTime() + (1000 * token.expires_in);
    Cookie.set('access_token', token.access_token, expireDate);
    this._router.navigate(['/']);
  }

  getResource(resourceUrl): Observable<Foo> {
    const headers = new HttpHeaders({
      'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
      Authorization: 'Bearer ' + Cookie.get('access_token')
    });

    return this._http.get<Foo>(resourceUrl, { headers });
  }

  checkCredentials() {
    if (!Cookie.check('access_token')) {
      this._router.navigate(['/login']);
    }
  }

  logout() {
    Cookie.delete('access_token');
    this._router.navigate(['/login']);
  }
}
