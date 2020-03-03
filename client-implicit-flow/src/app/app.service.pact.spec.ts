import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {OAuthLogger, OAuthService, UrlHelperService} from 'angular-oauth2-oidc';
import {PactWeb, Matchers, Interaction} from '@pact-foundation/pact-web';

import {AppService, Foo} from './app.service';

const expectedAccessToken = 'bar-token';

describe('AppService', () => {

  let provider;

  beforeAll(done => {
    provider = new PactWeb({
      consumer: 'client_implicit_flow', // todo: Passing in consumer/provider to PactWeb is deprecated
      provider: 'foo_service',
      port: 1234,
      host: '127.0.0.1',
    });

    // to make sure that no interactions from a previous test run linger in the mock server
    // required if run with `singleRun: false`
    provider.removeInteractions();

    // required for slower CI environments
    // setTimeout(done, 2000);
    done();
  });

  afterAll(done => {
    // tells the mock server to write all currently available interactions into a contract file
    provider.finalize()
      .then(() => done(), err => done.fail(err));
  });

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule,
      ],
      providers: [
        AppService,
        OAuthLogger,
        { provide: OAuthService, useClass: OAuthServiceMock },
        UrlHelperService,
      ],
    });
  });

  afterEach(done => {
    provider.verify().then(done, err => done.fail(err));
  });

  describe('getResource - foos', () => {

    const expectedFoo: Foo = {
      id: 333,
      name: 'foo-name-333',
    };

    const testUrl = '/spring-security-oauth-resource/foos/333';

    beforeAll(done => {

      const interaction = new Interaction()
          .given('foo 333 exists')
          .uponReceiving('a request to GET Foo 333')

          .withRequest({
            headers: {
              Authorization: `Bearer ${expectedAccessToken}`,
              'Content-Type': 'application/json;charset=UTF-8',
            },
            method: 'GET',
            path: testUrl,
          })

          .willRespondWith({
            body: Matchers.somethingLike(expectedFoo),
            headers: {
              'Content-Type': 'application/json',
            },
            status: 200,
          });

      provider.addInteraction(interaction)
        .then(done, err => done.fail(err));
    });

    it('should return Foo 333', done => {

      const appService: AppService = TestBed.get(AppService);

      appService.getResource(testUrl).subscribe(response => {
        expect(response).toEqual(expectedFoo);
        done();
      }, err => done.fail(err));
    });
  });

});

class OAuthServiceMock extends OAuthService {
  getAccessToken(): string {
    return expectedAccessToken;
  }
}
