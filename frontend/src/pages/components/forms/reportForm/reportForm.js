import React, { useState } from 'react';
import '../formsStyle.css';
import ReportTypes from '../../../../api/values/ReportTypes';
import { useTranslation } from 'react-i18next';

const ReportForm = ({ onCancel, onReportSubmit }) => {
  const { t } = useTranslation();
  const [error, setError] = useState(null);
  const [reportReason, setReportReason] = useState('');

  // Create mapping between translation keys and display text
  const reportReasonMappings = [
    { key: 'hate', displayText: t('reportForm.hate') },
    { key: 'abuseAndHarassment', displayText: t('reportForm.abuseAndHarassment') },
    { key: 'privacy', displayText: t('reportForm.privacy') },
    { key: 'spam', displayText: t('reportForm.spam') },
  ];

  // Map translation keys to ReportTypes keys
  const getReportTypeKey = (translationKey) => {
    const keyMapping = {
      'hate': 'Hate',
      'abuseAndHarassment': 'Abuse & Harassment',
      'privacy': 'Privacy',
      'spam': 'Spam',
    };
    return keyMapping[translationKey];
  };

  const handleSubmit = async () => {
    try {
      console.log('About to report');
      const reportTypeKey = getReportTypeKey(reportReason);
      const reportType = ReportTypes[reportTypeKey];
      const response = await onReportSubmit?.(reportType);
      console.log('reported', response);
      if (response) {
        if (response.status === 200) {
          onCancel();
        } else {
          console.log(response);
          setError(response.response.data.message);
        }
      }
    } catch (error) {
      setError(error.response?.data?.message || 'Error making request');
    }
  };

  return (
    <div className="overlay">
      <div className="box-review">
        {!error ? (
          <>
            <h2>{t('reportForm.report')}</h2>
            <div className="radio-group">
              {reportReasonMappings.map((mapping) => (
                <div key={mapping.key} className="radio-option">
                  <input
                    type="radio"
                    id={mapping.key}
                    name="reportReason"
                    value={mapping.key}
                    checked={reportReason === mapping.key}
                    onChange={(e) => setReportReason(e.target.value)}
                  />
                  <label htmlFor={mapping.key}>{mapping.displayText}</label>
                </div>
              ))}
            </div>
            <div className="buttons">
              <button className="cancel" onClick={onCancel}>
                {t('reportForm.cancel')}
              </button>
              <button className="submit" onClick={handleSubmit} disabled={!reportReason}>
                {t('reportForm.submitReport')}
              </button>
            </div>
          </>
        ) : (
          <>
            <h2 style={{ color: 'red' }}>{error}</h2>
            <button className="cancel" onClick={() => setError(null)}>
              {t('reportForm.back')}
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default ReportForm;
