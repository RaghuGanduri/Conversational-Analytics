import { Injectable }
from '@angular/core';

import { HttpClient }
from '@angular/common/http';

import { Observable }
from 'rxjs';

import { AnalyticsResponse }
from '../models/analytics-response';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  private apiUrl =
    'http://localhost:8080/api/analytics/query';

  constructor(
      private http: HttpClient) {
  }

  query(prompt: string):
      Observable<AnalyticsResponse> {

    return this.http.post<AnalyticsResponse>(
      this.apiUrl,
      {
        prompt
      }
    );
  }
}