import { Component }
from '@angular/core';

import { CommonModule }
from '@angular/common';

import { FormsModule }
from '@angular/forms';

import {
  NgxEchartsDirective
} from 'ngx-echarts';

import { AnalyticsService }
from '../../services/analytics';

import { AnalyticsResponse }
from '../../models/analytics-response';

@Component({
  selector: 'app-conversational-analytics',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    NgxEchartsDirective
  ],

  template: `
    <div class="container">

      <h1>
        Conversational Analytics
      </h1>

      <textarea
        [(ngModel)]="prompt"
        rows="5"
        placeholder="Ask analytics question">
      </textarea>

      <br><br>

      <button (click)="submit()">
        Submit
      </button>

      <br><br>

      <div *ngIf="loading">
        Loading...
      </div>

      <div *ngIf="response">

        <h3>
          Summary
        </h3>

        <p>
          {{ response.summary }}
        </p>

        <h3>
          Generated SQL
        </h3>

        <pre>
{{ response.generatedSql }}
        </pre>

        <!-- BAR / PIE CHART -->

        <div *ngIf="
            response.chartType === 'bar'
            || response.chartType === 'pie'
        ">

          <div
            echarts
            [options]="chartOptions"
            class="chart">
          </div>

        </div>

        <!-- TABLE -->

        <div *ngIf="
            response.chartType === 'table'
        ">

          <h3>
            Results
          </h3>

          <table>

            <thead>

              <tr>

                <th *ngFor="
                    let column of response.columns
                ">

                  {{ column }}

                </th>

              </tr>

            </thead>

            <tbody>

              <tr *ngFor="
                  let row of response.rows
              ">

                <td *ngFor="
                    let cell of row
                ">

                  {{ cell }}

                </td>

              </tr>

            </tbody>

          </table>

        </div>

      </div>

    </div>
  `,

  styles: [`
    .container {
      padding: 20px;
    }

    textarea {
      width: 100%;
      height: 120px;
      font-size: 16px;
    }

    button {
      padding: 10px 20px;
    }

    pre {
      background: #f5f5f5;
      padding: 10px;
      overflow-x: auto;
    }

    .chart {
      height: 500px;
      width: 100%;
      margin-top: 20px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 20px;
    }

    th {
      background: #f5f5f5;
    }

    th,
    td {
      border: 1px solid #ccc;
      padding: 10px;
      text-align: left;
    }
  `]
})
export class ConversationalAnalytics {

  prompt = '';

  response?: AnalyticsResponse;

  loading = false;

  chartOptions: any;

  constructor(
      private analyticsService:
          AnalyticsService) {
  }

  submit(): void {

    if (!this.prompt.trim()) {
      return;
    }

    this.loading = true;

    this.analyticsService
      .query(this.prompt)
      .subscribe({

        next: (res) => {

          console.log(res);

          this.response = res;

          this.prepareChart();

          this.loading = false;
        },

        error: (err) => {

          console.error(err);

          this.loading = false;
        }
      });
  }

  prepareChart(): void {

    if (!this.response) {
      return;
    }

    if (this.response.chartType === 'bar') {

      this.chartOptions = {

        xAxis: {
          type: 'category',
          data: this.response.labels
        },

        yAxis: {
          type: 'value'
        },

        series: [
          {
            data: this.response.values,
            type: 'bar'
          }
        ]
      };
    }

    if (this.response.chartType === 'pie') {

      this.chartOptions = {

        series: [
          {
            type: 'pie',

            label: {
              show: true,
              formatter: '{b}: {c}'
            },

            data:
              this.response.labels.map(
                (label, index) => ({

                  name: label,

                  value:
                    this.response?.values[index]
                }))
          }
      ]
      };
    }
  }
}